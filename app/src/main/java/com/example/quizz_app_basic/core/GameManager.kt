package com.example.quizz_app_basic.core

class GameManager(
    private val allQuestions: List<Question>,
    private val selectedTopics: List<String>,
    private val numberOfQuestions: Int,
    private val difficulty: Int,
    private val hintsEnabled: Boolean
) {

    companion object {
        private const val BASE_POINTS_CORRECT = 10
        private const val BONUS_TWO_STREAK = 5
        private const val PENALTY_INCORRECT = 3
    }

    private var questions: MutableList<QuestionState> = mutableListOf()
    var currentIndex = 0
        private set
    var score = 0
        private set
    var availableHints = if (hintsEnabled) 3 else 0
        private set
    private var consecutiveCorrect = 0

    val totalQuestions: Int
        get() = questions.size

    val answeredQuestions: Int
        get() = questions.count { it.selectedAnswer != null }

    fun startGame() {
        val filteredByTopic = allQuestions.filter { it.topic in selectedTopics }
        val source = if (filteredByTopic.isNotEmpty()) filteredByTopic else allQuestions
        val desiredAnswerCount = difficulty.coerceIn(2, 4)

        questions = source
            .shuffled()
            .take(numberOfQuestions.coerceAtMost(source.size))
            .map { question ->
                val shuffledIncorrect = question.incorrectAnswers.shuffled()
                val incorrectNeeded = (desiredAnswerCount - 1).coerceIn(1, shuffledIncorrect.size)
                val selectedIncorrect = shuffledIncorrect.take(incorrectNeeded)

                QuestionState(
                    question = question,
                    shuffledAnswers = (selectedIncorrect + question.correctAnswer).shuffled()
                )
            }
            .toMutableList()

        currentIndex = 0
        score = 0
        consecutiveCorrect = 0
        availableHints = if (hintsEnabled) 3 else 0
    }

    fun getCurrentQuestionState(): QuestionState? {
        return questions.getOrNull(currentIndex)
    }

    fun answerCurrentQuestion(selectedAnswer: String): Boolean {
        val state = getCurrentQuestionState() ?: return false
        if (state.selectedAnswer != null) return state.isCorrect == true

        val isCorrect = selectedAnswer == state.question.correctAnswer
        state.selectedAnswer = selectedAnswer
        state.isCorrect = isCorrect

        if (isCorrect) {
            consecutiveCorrect++
            var earned = BASE_POINTS_CORRECT

            if (consecutiveCorrect % 2 == 0) {
                earned += BONUS_TWO_STREAK
            }

            score += earned
        } else {
            consecutiveCorrect = 0
            score = (score - PENALTY_INCORRECT).coerceAtLeast(0)
        }

        return isCorrect
    }

    fun useHintOnCurrentQuestion(): List<String> {
        val state = getCurrentQuestionState() ?: return emptyList()
        if (!hintsEnabled || availableHints <= 0 || state.usedHint || state.selectedAnswer != null) {
            return state.eliminatedAnswers.toList()
        }

        val removableWrongAnswers = state.shuffledAnswers
            .filter { it != state.question.correctAnswer }
            .shuffled()
            .take(2)

        state.eliminatedAnswers.addAll(removableWrongAnswers)
        state.usedHint = true
        availableHints--

        return removableWrongAnswers
    }

    fun moveToNextQuestion(): Boolean {
        if (currentIndex + 1 >= questions.size) return false
        currentIndex++
        return true
    }

    fun isGameFinished(): Boolean {
        return questions.isNotEmpty() && answeredQuestions >= questions.size
    }

    fun getFinalScore(): Int {
        return score
    }

}