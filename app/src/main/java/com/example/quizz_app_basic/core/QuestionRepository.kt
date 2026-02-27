package com.example.quizz_app_basic.core

object QuestionRepository {

    fun getAll(): List<Question> {
        return listOf(
            Question(
                1,
                "Historia",
                "¿Quién descubrió América?",
                "Cristóbal Colón",
                listOf("Napoleón", "Einstein", "Newton")
            )
        )
    }
}
