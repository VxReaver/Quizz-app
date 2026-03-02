package com.example.quizz_app_basic.ui

import androidx.lifecycle.ViewModel

class OptionsViewModel : ViewModel() {
    val selectedThemes = mutableSetOf<Int>()
    var questionCount: Float = 5f
    var difficultyPosition: Int = 0
    var isHintsEnabled: Boolean = false
}