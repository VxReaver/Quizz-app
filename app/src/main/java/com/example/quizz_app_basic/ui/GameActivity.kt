package com.example.quizz_app_basic.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizz_app_basic.databinding.ActivityGameBinding
import com.example.quizz_app_basic.model.Question
import com.example.quizz_app_basic.viewmodel.GameViewModel

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel
    private var questionAnswered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        loadMockQuestion()
        setupListeners()

        viewModel.currentQuestion.observe(this) { question ->
            showQuestion(question)
            questionAnswered = false
            enableButtons()
        }

        viewModel.questionCounter.observe(this) { counter ->
            binding.tvCounter.text = counter
        }



    }

    private fun enableButtons() {
        binding.btnOption1.isEnabled = true
        binding.btnOption2.isEnabled = true
        binding.btnOption3.isEnabled = true
        binding.btnOption4.isEnabled = true
    }






    private fun loadMockQuestion() {
        val question = Question(
            id = 1,
            topic = "Historia",
            text = "¿Quién descubrió América?",
            correctAnswer = "Cristóbal Colón",
            incorrectAnswers = listOf("Napoleón", "Einstein", "Newton")
        )

        showQuestion(question)
    }

    private fun showQuestion(question: Question) {
        binding.tvQuestion.text = question.text

        val options = mutableListOf<String>()
        options.add(question.correctAnswer)
        options.addAll(question.incorrectAnswers)
        options.shuffle()

        binding.btnOption1.text = options[0]
        binding.btnOption2.text = options[1]
        binding.btnOption3.text = options[2]
        binding.btnOption4.text = options[3]
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
        if (questionAnswered) return

        questionAnswered = true

        viewModel.answerQuestion(selectedAnswer)

        val correctAnswer = viewModel.currentQuestion.value?.correctAnswer

        if (selectedAnswer == correctAnswer) {
            Toast.makeText(this, "Correcta", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Incorrecta", Toast.LENGTH_SHORT).show()
        }

        disableButtons()
    }

    private fun disableButtons() {
        binding.btnOption1.isEnabled = false
        binding.btnOption2.isEnabled = false
        binding.btnOption3.isEnabled = false
        binding.btnOption4.isEnabled = false
    }
}
