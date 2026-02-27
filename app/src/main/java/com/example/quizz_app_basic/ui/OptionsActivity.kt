package com.example.quizz_app_basic.ui

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

    // Requiere la dependencia activity-ktx en build.gradle
    private val viewModel: OptionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        // Recuperación segura del estado (Fallo 4 corregido)
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

        // Sincronización segura (Fallo 5 corregido con post para el Slider)
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
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupListeners() {
        findViewById<Slider>(R.id.slider_questions).addOnChangeListener { _, value, _ ->
            viewModel.questionCount = value
        }

        findViewById<MaterialSwitch>(R.id.switch_hints).setOnCheckedChangeListener { _, isChecked ->
            viewModel.isHintsEnabled = isChecked
        }

        getCheckboxIds().forEach { id ->
            findViewById<CheckBox>(id)?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.selectedThemes.add(id)
                else viewModel.selectedThemes.remove(id)
            }
        }
    }

    private fun syncViewWithViewModel() {
        val slider = findViewById<Slider>(R.id.slider_questions)
        // Usamos post para asegurar que el slider ya conoce su rango (valueFrom/To)
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
}