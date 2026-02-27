class GameManager(
    private val allQuestions: List<Question>,
    private val selectedTopics: List<String>,
    private val numberOfQuestions: Int,
    private val difficulty: Int,
    private val hintsEnabled: Boolean
) {

    private var questions: MutableList<QuestionState> = mutableListOf()
    var currentIndex = 0
    var score = 0
    var availableHints = if (hintsEnabled) 3 else 0
    private var consecutiveCorrect = 0

}