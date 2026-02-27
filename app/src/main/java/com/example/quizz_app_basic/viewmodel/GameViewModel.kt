import androidx.lifecycle.ViewModel
import com.example.quizz_app_basic.core.*

class GameViewModel : ViewModel() {

    lateinit var gameManager: GameManager

    fun initGame(
        allQuestions: List<Question>,
        selectedTopics: List<String>,
        numberOfQuestions: Int,
        difficulty: Int,
        hintsEnabled: Boolean
    ) {
        if (!::gameManager.isInitialized) {
            gameManager = GameManager(
                allQuestions,
                selectedTopics,
                numberOfQuestions,
                difficulty,
                hintsEnabled
            )
            gameManager.startGame()
        }
    }
}