package com.example.quizz_app_basic

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
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

        if (!viewModel.isGameInitialized()) {
            val selectedTopics = intent.getStringArrayListExtra("TOPICS") ?: arrayListOf("Historia")

            viewModel.initGame(
                QuestionRepository.getAll(),
                selectedTopics,
                intent.getIntExtra("NUM_QUESTIONS", 5),
                intent.getIntExtra("DIFFICULTY", 4),
                intent.getBooleanExtra("HINTS", true)
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}