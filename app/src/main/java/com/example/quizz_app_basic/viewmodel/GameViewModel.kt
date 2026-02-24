package com.example.quizz_app_basic.viewmodel

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
}