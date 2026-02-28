package com.example.quizz_app_basic.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizz_app_basic.databinding.ActivityGameBinding
import com.example.quizz_app_basic.model.GameQuestion
import com.example.quizz_app_basic.model.Question
import com.example.quizz_app_basic.viewmodel.GameViewModel
import android.os.Handler
import android.os.Looper
import androidx.activity.addCallback


class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel
    private var questionAnswered = false

    private var currentVisibleOptions: List<String> = emptyList()

    private var doubleBackToExitPressedOnce = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        loadMockQuestion()
        setupListeners()

        viewModel.currentQuestion.observe(this) { gameQuestion ->
            gameQuestion?.let {
                showQuestion(it)
                questionAnswered = false
                enableButtons()
                resetButtonColors()
            }
        }


        viewModel.questionCounter.observe(this) { counter ->
            binding.tvCounter.text = counter
        }

        binding.btnHint.setOnClickListener {

            val currentQuestion = viewModel.getCurrentQuestion() ?: return@setOnClickListener

            val optionToRemove = viewModel.useHint(
                currentVisibleOptions,
                currentQuestion.question.correctAnswer

            )

            if (optionToRemove != null) {
                removeOptionFromUI(optionToRemove)
                updateHintsUI()
            }

            if (viewModel.remainingHints == 0) {
                binding.btnHint.isEnabled = false
            }
        }

        binding.btnPrev.setOnClickListener {
            viewModel.previousQuestion()
        }

        onBackPressedDispatcher.addCallback(this) {

            if (doubleBackToExitPressedOnce) {
                finish()
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(this@GameActivity, "Presiona nuevamente para salir", Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)
            }
        }






        updateHintsUI()



    }

    private fun enableButtons() {
        binding.btnOption1.isEnabled = true
        binding.btnOption2.isEnabled = true
        binding.btnOption3.isEnabled = true
        binding.btnOption4.isEnabled = true
    }






    private fun loadMockQuestion() {
        val question = Question(

            text = "¿Quién descubrió América?",
            correctAnswer = "Cristóbal Colón",
            incorrectAnswers = listOf("Napoleón", "Einstein", "Newton")
        )

        showQuestion(GameQuestion(question))
    }

    private fun showQuestion(gameQuestion: GameQuestion) {
        binding.tvQuestion.text = gameQuestion.question.text
        binding.tvHintUsed.visibility = if (gameQuestion.hintUsed) View.VISIBLE else View.GONE

        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)

        // 1. Configurar textos y visibilidad según el modelo
        currentVisibleOptions = gameQuestion.displayOptions
        buttons.forEach { it.visibility = View.GONE } // Ocultar todos por defecto

        for (i in currentVisibleOptions.indices) {
            val optionText = currentVisibleOptions[i]
            buttons[i].apply {
                text = optionText
                // Si la opción fue eliminada por una pista, se mantiene oculta
                visibility = if (optionText in gameQuestion.removedOptions) View.GONE else View.VISIBLE
                // Bloquear clic si ya se respondió esta pregunta
                isEnabled = gameQuestion.selectedAnswer == null
            }
        }

        // 2. Limpiar colores previos
        resetButtonColors()

        // 3. SI YA FUE RESPONDIDA: Restaurar el "pintado" de acierto/error
        gameQuestion.selectedAnswer?.let { selected ->
            val correct = gameQuestion.question.correctAnswer

            // Pintamos la correcta siempre de verde
            markCorrect(correct)

            // Si el usuario falló, pintamos la suya de rojo
            if (selected != correct) {
                markIncorrect(selected)
            }

            binding.btnHint.isEnabled = false // No usar pistas en preguntas terminadas
        } ?: run {
            // Si NO ha sido respondida, habilitar pista si quedan disponibles
            binding.btnHint.isEnabled = viewModel.remainingHints > 0
        }
    }


    private fun setupListeners() {
        binding.btnOption1.setOnClickListener { checkAnswer(binding.btnOption1.text.toString()) }
        binding.btnOption2.setOnClickListener { checkAnswer(binding.btnOption2.text.toString()) }
        binding.btnOption3.setOnClickListener { checkAnswer(binding.btnOption3.text.toString()) }
        binding.btnOption4.setOnClickListener { checkAnswer(binding.btnOption4.text.toString()) }

        binding.btnNext.setOnClickListener {
            viewModel.nextQuestion()
        }
    }

    private fun checkAnswer(selectedAnswer: String) {
        val currentGQ = viewModel.getCurrentQuestion() ?: return
        if (currentGQ.selectedAnswer != null) return // Evita re-contestar

        viewModel.answerQuestion(selectedAnswer)

        // Refrescar UI llamando de nuevo a showQuestion o aplicando lógica manual:
        showQuestion(currentGQ)
    }


    private fun disableButtons() {
        binding.btnOption1.isEnabled = false
        binding.btnOption2.isEnabled = false
        binding.btnOption3.isEnabled = false
        binding.btnOption4.isEnabled = false
    }


    private fun markCorrect(answer: String) {
        when (answer) {
            binding.btnOption1.text.toString() ->
                binding.btnOption1.setBackgroundColor(getColor(android.R.color.holo_green_light))

            binding.btnOption2.text.toString() ->
                binding.btnOption2.setBackgroundColor(getColor(android.R.color.holo_green_light))

            binding.btnOption3.text.toString() ->
                binding.btnOption3.setBackgroundColor(getColor(android.R.color.holo_green_light))

            binding.btnOption4.text.toString() ->
                binding.btnOption4.setBackgroundColor(getColor(android.R.color.holo_green_light))
        }
    }

    private fun markIncorrect(answer: String) {
        when (answer) {
            binding.btnOption1.text.toString() ->
                binding.btnOption1.setBackgroundColor(getColor(android.R.color.holo_red_light))

            binding.btnOption2.text.toString() ->
                binding.btnOption2.setBackgroundColor(getColor(android.R.color.holo_red_light))

            binding.btnOption3.text.toString() ->
                binding.btnOption3.setBackgroundColor(getColor(android.R.color.holo_red_light))

            binding.btnOption4.text.toString() ->
                binding.btnOption4.setBackgroundColor(getColor(android.R.color.holo_red_light))
        }


    }

    private fun resetButtonColors() {
        val defaultColor = getColor(android.R.color.darker_gray)

        binding.btnOption1.setBackgroundColor(defaultColor)
        binding.btnOption2.setBackgroundColor(defaultColor)
        binding.btnOption3.setBackgroundColor(defaultColor)
        binding.btnOption4.setBackgroundColor(defaultColor)
    }


    private fun removeOptionFromUI(option: String) {

        val buttons = listOf(
            binding.btnOption1,
            binding.btnOption2,
            binding.btnOption3,
            binding.btnOption4
        )

        buttons.forEach {
            if (it.text == option) {
                it.visibility = android.view.View.GONE
            }
        }
    }

    private fun updateHintsUI() {
        binding.tvHints.text = "Pistas: ${viewModel.remainingHints}"
    }








}
