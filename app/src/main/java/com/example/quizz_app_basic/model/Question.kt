package com.example.quizz_app_basic.model

data class Question(
    val text: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val topic: String = "" // Añadimos el tema al modelo de la UI
)
