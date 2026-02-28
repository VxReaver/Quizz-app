package com.example.quizz_app_basic.model

data class GameQuestion(
    val question: Question,
    var selectedAnswer: String? = null,
    var isCorrect: Boolean? = null,
    var hintUsed: Boolean = false,
    var displayOptions: List<String> = emptyList(), // GUARDAR EL ORDEN
    val removedOptions: MutableList<String> = mutableListOf() // OPCIONES OCULTAS POR PISTA
)

