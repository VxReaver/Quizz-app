# Guía rápida: integrar GameManager en Activity 3 (Juego)

## 1) Obtener estado actual
En GameActivity, toma el estado con:

```kotlin
val state = viewModel.gameManager.getCurrentQuestionState() ?: return
```

Muestra en UI:
- `state.question.questionText`
- `state.shuffledAnswers` (ocultando las que estén en `state.eliminatedAnswers`)

## 2) Responder una pregunta
Cuando el usuario toque una opción:

```kotlin
val isCorrect = viewModel.gameManager.answerCurrentQuestion(selectedAnswer)
```

Luego:
- Bloquea botones de respuesta de esa pregunta
- Muestra feedback (correcta/incorrecta)
- Actualiza puntaje con `viewModel.gameManager.score`

## 3) Usar pista
En botón de pista:

```kotlin
val removed = viewModel.gameManager.useHintOnCurrentQuestion()
```

- Si `removed` trae respuestas, ocúltalas en UI
- Actualiza contador visual con `viewModel.gameManager.availableHints`

## 4) Ir a siguiente pregunta
En botón "Siguiente":

```kotlin
val hasNext = viewModel.gameManager.moveToNextQuestion()
if (hasNext) {
    renderCurrentQuestion()
} else {
    startActivity(Intent(this, ResultActivity::class.java).apply {
        putExtra("FINAL_SCORE", viewModel.gameManager.getFinalScore())
    })
    finish()
}
```

## 5) Reglas ya cubiertas por GameManager
- Dificultad: 2, 3 o 4 respuestas por pregunta
- Pistas: elimina 2 incorrectas (si hay disponibles)
- Bono: +5 cada 2 correctas seguidas
- Penalización: -3 por incorrecta (sin bajar de 0)
- Base por correcta: +10
