package com.example.quizz_app_basic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizz_app_basic.core.GameManager
import com.example.quizz_app_basic.core.Question as CoreQuestion
import com.example.quizz_app_basic.model.Difficulty
import com.example.quizz_app_basic.model.GameQuestion
import com.example.quizz_app_basic.model.Question as UiQuestion

class GameViewModel : ViewModel() {

    private var questions: List<GameQuestion> = emptyList()

    lateinit var gameManager: GameManager

    var currentQuestionIndex: Int = 0
        private set

    fun isGameInitialized(): Boolean {
        return ::gameManager.isInitialized
    }

    fun initGame(
        allQuestions: List<CoreQuestion>,
        selectedTopics: List<String>,
        numberOfQuestions: Int,
        difficulty: Int,
        hintsEnabled: Boolean
    ) {
        if (!::gameManager.isInitialized) {
            gameManager = GameManager(
                allQuestions,
                selectedTopics,
                numberOfQuestions,
                difficulty,
                hintsEnabled
            )
            gameManager.startGame()
        }

        val parsedDifficulty = when (difficulty) {
            0 -> Difficulty.EASY
            1 -> Difficulty.NORMAL
            2 -> Difficulty.EASY
            3 -> Difficulty.NORMAL
            else -> Difficulty.HARD
        }

        initializeQuestions(
            allQuestions = allQuestions,
            selectedTopics = selectedTopics,
            numberOfQuestions = numberOfQuestions,
            selectedDifficulty = parsedDifficulty,
            hintsEnabled = hintsEnabled
        )
    }

    var score: Int = 0
        private set

    var remainingHints: Int = 3
        private set

    var hintUsedInCurrentQuestion: Boolean = false
        private set


    var difficulty: Difficulty = Difficulty.NORMAL

    private val _currentQuestion = MutableLiveData<GameQuestion?>()
    val currentQuestion: LiveData<GameQuestion?> = _currentQuestion


    private val _questionCounter = MutableLiveData<String>()
    val questionCounter: LiveData<String> = _questionCounter

    fun initializeQuestions(
        allQuestions: List<CoreQuestion>,
        selectedTopics: List<String>,
        numberOfQuestions: Int,
        selectedDifficulty: Difficulty,
        hintsEnabled: Boolean
    ) {
        difficulty = selectedDifficulty
        remainingHints = if (hintsEnabled) 3 else 0
        hintUsedInCurrentQuestion = false
        score = 0
        currentQuestionIndex = 0

        // FILTRADO CORRECTO POR TEMAS
        val filteredByTopic = if (selectedTopics.isEmpty()) {
            allQuestions
        } else {
            allQuestions.filter { it.topic in selectedTopics }
        }

        // Si no hay preguntas para los temas seleccionados (por seguridad), usamos todas
        val source = if (filteredByTopic.isNotEmpty()) {
            filteredByTopic
        } else {
            allQuestions
        }

        questions = source
            .shuffled()
            .take(numberOfQuestions.coerceAtLeast(1).coerceAtMost(source.size))
            .map { coreQuestion ->
                GameQuestion(
                    UiQuestion(
                        text = coreQuestion.questionText,
                        correctAnswer = coreQuestion.correctAnswer,
                        incorrectAnswers = coreQuestion.incorrectAnswers,
                        topic = coreQuestion.topic // PASAR EL TEMA
                    )
                )
            }

        loadCurrentQuestion()
    }


    private fun loadCurrentQuestion() {
        if (questions.isEmpty()) {
            _currentQuestion.value = null
            _questionCounter.value = "Pregunta 0/0"
            return
        }

        if (questions.isNotEmpty()) {
            val gq = questions[currentQuestionIndex]

            // Si las opciones están vacías, las generamos por primera y única vez
            if (gq.displayOptions.isEmpty()) {
                val correct = gq.question.correctAnswer
                val incorrect = gq.question.incorrectAnswers.shuffled()
                val numOptions = difficulty.numberOfOptions

                // Mezclamos la correcta con las incorrectas según la dificultad
                gq.displayOptions = (incorrect.take(numOptions - 1) + correct).shuffled()
            }

            _currentQuestion.value = gq
            _questionCounter.value = "Pregunta ${currentQuestionIndex + 1}/${questions.size}"
        }
    }

    fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {

            hintUsedInCurrentQuestion = false
            currentQuestionIndex++
            loadCurrentQuestion()
        }
    }

    fun answerQuestion(selectedAnswer: String) {
        val gameQuestion = questions[currentQuestionIndex]
        val correctAnswer = gameQuestion.question.correctAnswer

        gameQuestion.selectedAnswer = selectedAnswer
        gameQuestion.isCorrect = selectedAnswer == correctAnswer
    }


    fun useHint(currentOptions: List<String>, correctAnswer: String): String? {
        if (remainingHints <= 0) return null

        val gq = questions[currentQuestionIndex]
        // Filtrar opciones que no sean la correcta y que no hayan sido ya eliminadas
        val availableIncorrect = currentOptions.filter { it != correctAnswer && it !in gq.removedOptions }

        if (availableIncorrect.isEmpty()) return null

        val optionToRemove = availableIncorrect.random()
        remainingHints--

        // Guardar estado en la pregunta actual
        gq.hintUsed = true
        gq.removedOptions.add(optionToRemove)
        hintUsedInCurrentQuestion = true // Para la UI inmediata

        return optionToRemove
    }

    fun getCurrentQuestion(): GameQuestion? {
        if (questions.isEmpty()) return null
        if (currentQuestionIndex >= questions.size) return null
        return questions[currentQuestionIndex]
    }

    fun previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            loadCurrentQuestion()
        }
    }

    fun isLastQuestion(): Boolean = currentQuestionIndex == questions.size - 1

    fun allQuestionsAnswered(): Boolean = questions.all { it.selectedAnswer != null }

    fun calculateFinalScore(): Int {
        var total = 0
        val pointsPerCorrect = when (difficulty) {
            Difficulty.EASY -> 10
            Difficulty.NORMAL -> 15
            Difficulty.HARD -> 20
        }

        var totalHintsUsed = 0
        questions.forEach { q ->
            if (q.isCorrect == true) {
                total += pointsPerCorrect
            }
            if (q.hintUsed) totalHintsUsed++
        }

        // Penalización por pistas usadas
        total -= (totalHintsUsed * 2)

        // Bonificación por pistas no utilizadas (de las 3 iniciales)
        total += (remainingHints * 5)

        return total.coerceAtLeast(0)
    }

    fun getHintsUsedTotal(): Int {
        return questions.count { it.hintUsed }
    }
}
