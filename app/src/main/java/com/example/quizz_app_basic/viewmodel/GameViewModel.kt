package com.example.quizz_app_basic.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quizz_app_basic.core.GameManager
import com.example.quizz_app_basic.core.Question

class GameViewModel : ViewModel() {

	lateinit var gameManager: GameManager

	fun isGameInitialized(): Boolean {
		return ::gameManager.isInitialized
	}

	fun initGame(
		allQuestions: List<Question>,
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
	}
}
