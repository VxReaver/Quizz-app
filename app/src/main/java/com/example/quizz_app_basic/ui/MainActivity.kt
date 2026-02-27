package com.example.quizz_app_basic.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.quizz_app_basic.R
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // id botones
        val btnPlay = findViewById<MaterialButton>(R.id.button_play)
        val btnOptions = findViewById<MaterialButton>(R.id.button_options)
        val btnLeaderboard = findViewById<MaterialButton>(R.id.button_leaderboard)

        // deshabilitacion de boton puntuaciones
        btnLeaderboard.isEnabled = false


        //Navegacion
        //ugar -> Activity 3
        btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        //Opciones -> Activity 2
        btnOptions.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("anim_done", viewModel.isInitialAnimationDone)
    }
}