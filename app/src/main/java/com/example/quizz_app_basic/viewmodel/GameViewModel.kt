package com.example.quizz_app_basic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizz_app_basic.model.Difficulty
import com.example.quizz_app_basic.model.GameQuestion
import com.example.quizz_app_basic.model.Question

class GameViewModel : ViewModel() {

    private var questions: List<GameQuestion> = emptyList()

    var currentQuestionIndex: Int = 0
        private set

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

    init {
        loadMockQuestions()
        loadCurrentQuestion()
    }

    private fun loadMockQuestions() {
        questions = listOf(
            GameQuestion(
                Question(
                    text = "¿Quién descubrió América?",
                    correctAnswer = "Cristóbal Colón",
                    incorrectAnswers = listOf("Napoleón", "Einstein", "Newton")
                )
            ),
            GameQuestion(
                Question(
                    text = "¿Cuál es el planeta rojo?",
                    correctAnswer = "Marte",
                    incorrectAnswers = listOf("Venus", "Júpiter", "Saturno")
                )
            ),
            GameQuestion(
                Question(
                    text = "¿Capital de Francia?",
                    correctAnswer = "París",
                    incorrectAnswers = listOf("Roma", "Madrid", "Berlín")
                )
            ),
            GameQuestion(
                Question(
                    text = "¿Cuánto es 2 + 2?",
                    correctAnswer = "4",
                    incorrectAnswers = listOf("3", "5", "6")
                )
            ),
            GameQuestion(
                Question(
                    text = "¿Quién pintó la Mona Lisa?",
                    correctAnswer = "Leonardo da Vinci",
                    incorrectAnswers = listOf("Picasso", "Van Gogh", "Dalí")
                )
            )
        )
    }


    private fun loadCurrentQuestion() {
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



}
