package com.example.quizz_app_basic.core

data class Question(
    val id: Int,
    val topic: String,
    val questionText: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)