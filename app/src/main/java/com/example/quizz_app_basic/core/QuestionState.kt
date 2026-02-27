data class QuestionState(
    val question: Question,
    var shuffledAnswers: List<String>,
    var selectedAnswer: String? = null,
    var isCorrect: Boolean? = null,
    var usedHint: Boolean = false,
    var eliminatedAnswers: MutableSet<String> = mutableSetOf()
)