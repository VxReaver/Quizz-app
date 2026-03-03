package com.example.quizz_app_basic.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.quizz_app_basic.R
import com.google.android.material.slider.Slider
import com.google.android.material.materialswitch.MaterialSwitch
import java.util.ArrayList // Import explícito para evitar fallo 3

class OptionsActivity : AppCompatActivity() {

    private val viewModel: OptionsViewModel by viewModels()

    companion object {
        const val PREFS_NAME = "quiz_options"
        const val KEY_NUM_QUESTIONS = "num_questions"
        const val KEY_DIFFICULTY_POSITION = "difficulty_position"
        const val KEY_HINTS_ENABLED = "hints_enabled"
        const val KEY_TOPICS = "topics"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        loadFromPreferences()

        savedInstanceState?.let { bundle ->
            viewModel.questionCount = bundle.getFloat("SLIDER_VALUE", 5f)
            viewModel.isHintsEnabled = bundle.getBoolean("SWITCH_VALUE", false)
            viewModel.difficultyPosition = bundle.getInt("SPINNER_POS", 0)

            bundle.getIntegerArrayList("THEMES_LIST")?.let { list ->
                viewModel.selectedThemes.clear()
                viewModel.selectedThemes.addAll(list)
            }
        }

        setupDifficultySpinner()

        syncViewWithViewModel()
        setupListeners()
    }

    private fun setupDifficultySpinner() {
        val spinner: Spinner = findViewById(R.id.spinner_difficulty)
        val options = arrayOf("Fácil", "Normal", "Difícil")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        spinner.adapter = adapter
        spinner.setSelection(viewModel.difficultyPosition)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.difficultyPosition = position
                saveToPreferences()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupListeners() {
        findViewById<Slider>(R.id.slider_questions).addOnChangeListener { _, value, _ ->
            viewModel.questionCount = value
            saveToPreferences()
        }

        findViewById<MaterialSwitch>(R.id.switch_hints).setOnCheckedChangeListener { _, isChecked ->
            viewModel.isHintsEnabled = isChecked
            saveToPreferences()
        }

        getCheckboxIds().forEach { id ->
            findViewById<CheckBox>(id)?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.selectedThemes.add(id)
                else viewModel.selectedThemes.remove(id)
                saveToPreferences()
            }
        }
    }

    private fun syncViewWithViewModel() {
        val slider = findViewById<Slider>(R.id.slider_questions)
        slider.post {
            slider.value = viewModel.questionCount.coerceIn(slider.valueFrom, slider.valueTo)
        }

        findViewById<MaterialSwitch>(R.id.switch_hints).isChecked = viewModel.isHintsEnabled
        findViewById<Spinner>(R.id.spinner_difficulty).setSelection(viewModel.difficultyPosition)

        getCheckboxIds().forEach { id ->
            findViewById<CheckBox>(id)?.isChecked = viewModel.selectedThemes.contains(id)
        }
    }

    private fun getCheckboxIds() = listOf(
        R.id.checkbox_music,
        R.id.checkbox_sports,
        R.id.checkbox_history,
        R.id.checkbox_generalKnowledge,
        R.id.checkbox_entertainment
    )

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("SLIDER_VALUE", viewModel.questionCount)
        outState.putBoolean("SWITCH_VALUE", viewModel.isHintsEnabled)
        outState.putInt("SPINNER_POS", viewModel.difficultyPosition)
        // Fallo 3 corregido: Uso de java.util.ArrayList explícito
        outState.putIntegerArrayList("THEMES_LIST", ArrayList(viewModel.selectedThemes.toList()))
    }

    override fun onPause() {
        super.onPause()
        saveToPreferences()
    }

    private fun loadFromPreferences() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        viewModel.questionCount = prefs.getInt(KEY_NUM_QUESTIONS, 5).toFloat()
        viewModel.difficultyPosition = prefs.getInt(KEY_DIFFICULTY_POSITION, 1)
        viewModel.isHintsEnabled = prefs.getBoolean(KEY_HINTS_ENABLED, true)

        val storedTopics = prefs.getStringSet(KEY_TOPICS, emptySet()).orEmpty()
        viewModel.selectedThemes.clear()

        if (storedTopics.isEmpty()) {
            return
        }

        getCheckboxIds().forEach { id ->
            if (getTopicFromCheckboxId(id) in storedTopics) {
                viewModel.selectedThemes.add(id)
            }
        }
    }

    private fun saveToPreferences() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val selectedTopics = viewModel.selectedThemes
            .mapNotNull { getTopicFromCheckboxId(it) }
            .toSet()

        prefs.edit()
            .putInt(KEY_NUM_QUESTIONS, viewModel.questionCount.toInt())
            .putInt(KEY_DIFFICULTY_POSITION, viewModel.difficultyPosition)
            .putBoolean(KEY_HINTS_ENABLED, viewModel.isHintsEnabled)
            .putStringSet(KEY_TOPICS, selectedTopics)
            .apply()
    }

    private fun getTopicFromCheckboxId(id: Int): String? {
        return when (id) {
            R.id.checkbox_music -> "Música"
            R.id.checkbox_sports -> "Deportes"
            R.id.checkbox_history -> "Historia"
            R.id.checkbox_generalKnowledge -> "Cultura General"
            R.id.checkbox_entertainment -> "Entretenimiento"
            else -> null
        }
    }
}