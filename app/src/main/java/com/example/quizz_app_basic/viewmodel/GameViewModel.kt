package com.example.quizz_app_basic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizz_app_basic.model.Difficulty
import com.example.quizz_app_basic.model.Question

class GameViewModel : ViewModel() {

    private var questions: List<Question> = emptyList()

    var currentQuestionIndex: Int = 0
        private set

    var score: Int = 0
        private set

    var remainingHints: Int = 3
        private set

    var difficulty: Difficulty = Difficulty.NORMAL

    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question> = _currentQuestion

    private val _questionCounter = MutableLiveData<String>()
    val questionCounter: LiveData<String> = _questionCounter

    init {
        loadMockQuestions()
        loadCurrentQuestion()
    }

    private fun loadMockQuestions() {
        questions = listOf(
            Question(1, "Historia", "¿Quién descubrió América?", "Cristóbal Colón", listOf("Napoleón", "Einstein", "Newton")),
            Question(2, "Ciencia", "¿Cuál es el planeta rojo?", "Marte", listOf("Venus", "Júpiter", "Saturno")),
            Question(3, "Geografía", "¿Capital de Francia?", "París", listOf("Roma", "Madrid", "Berlín")),
            Question(4, "Matemáticas", "¿Cuánto es 2 + 2?", "4", listOf("3", "5", "6")),
            Question(5, "Arte", "¿Quién pintó la Mona Lisa?", "Leonardo da Vinci", listOf("Picasso", "Van Gogh", "Dalí"))
        )
    }

    private fun loadCurrentQuestion() {
        if (questions.isNotEmpty()) {
            _currentQuestion.value = questions[currentQuestionIndex]
            _questionCounter.value =
                "Pregunta ${currentQuestionIndex + 1}/${questions.size}"
        }
    }

    fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            loadCurrentQuestion()
        }
    }

    fun answerQuestion(selectedAnswer: String) {
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        if (selectedAnswer == correctAnswer) {
            score++
        }
    }
}
