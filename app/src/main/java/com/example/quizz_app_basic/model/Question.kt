package com.example.quizz_app_basic.model

data class Question(
    val id: Int,
    val topic: String,
    val text: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)
