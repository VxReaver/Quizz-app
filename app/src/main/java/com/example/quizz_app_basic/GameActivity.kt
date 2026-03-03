package com.example.quizz_app_basic

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizz_app_basic.core.QuestionRepository
import com.example.quizz_app_basic.viewmodel.GameViewModel

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)

        setupBackground()

        if (!viewModel.isGameInitialized()) {
            val selectedTopics = intent.getStringArrayListExtra("TOPICS") ?: arrayListOf()
            val numQuestions = intent.getIntExtra("NUM_QUESTIONS", 5)
            val difficulty = intent.getIntExtra("DIFFICULTY_POSITION", 1)
            val hintsEnabled = intent.getBooleanExtra("HINTS", true)

            viewModel.initGame(
                QuestionRepository.getAll(),
                selectedTopics,
                numQuestions,
                difficulty,
                hintsEnabled
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBackground() {
        val backgroundImage = findViewById<ImageView>(R.id.background_image)
        val topics = intent.getStringArrayListExtra("TOPICS") ?: arrayListOf()
        val currentTopic = topics.firstOrNull() ?: ""

        val backgroundRes = when (currentTopic) {
            "Historia" -> R.drawable.fondo_historia
            "Música" -> R.drawable.fondo_musica
            "Deportes" -> R.drawable.fondo_deportes
            "Cultura General" -> R.drawable.fondo_culturageneral
            "Entretenimiento" -> R.drawable.fondo_entretenimiento
            else -> R.drawable.fondo_default
        }
        backgroundImage.setImageResource(backgroundRes)
    }
}
