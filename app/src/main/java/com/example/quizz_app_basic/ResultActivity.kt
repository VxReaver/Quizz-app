package com.example.quizz_app_basic

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val score = intent.getIntExtra("SCORE", 0)
        val hintsUsed = intent.getIntExtra("HINTS_USED", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 5)

        val tvScore = findViewById<TextView>(R.id.tv_final_score)
        val ivResult = findViewById<ImageView>(R.id.iv_result_image)
        val btnMenu = findViewById<Button>(R.id.btn_back_to_menu)

        tvScore.text = "Puntuación final: $score\nPistas utilizadas: $hintsUsed"

        // Lógica de imagen personalizada basada en el puntaje
        val maxPossibleWithoutHints = totalQuestions * 20 // Estimación para Hard
        val performance = score.toFloat() / (maxPossibleWithoutHints.coerceAtLeast(1))

        when {
            performance >= 0.8 -> {
                ivResult.setImageResource(android.R.drawable.btn_star_big_on)
            }
            performance >= 0.5 -> {
                ivResult.setImageResource(android.R.drawable.ic_dialog_info)
            }
            else -> {
                ivResult.setImageResource(android.R.drawable.ic_delete)
            }
        }

        btnMenu.setOnClickListener {
            finish()
        }
    }
}
