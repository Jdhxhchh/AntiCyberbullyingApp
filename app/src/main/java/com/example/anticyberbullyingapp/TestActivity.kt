package com.example.anticyberbullyingapp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.anticyberbullyingapp.databinding.ActivityTestBinding

// Додаємо клас TestQuestion в тому ж файлі
data class TestQuestion(
    val id: Int,
    val text: String,
    val answers: List<String> = listOf("Ніколи", "Рідко", "Іноді", "Часто", "Завжди")
)

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private val questions = mutableListOf<TestQuestion>()
    private val userAnswers = mutableMapOf<Int, Int>()
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadQuestions() // Ініціалізація питань
        setupQuestion() // Налаштування першого питання

        binding.nextButton.setOnClickListener {
            saveAnswer()
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                setupQuestion()
            } else {
                showResult()
            }
        }
    }

    private fun loadQuestions() {
        // Тут ваш список питань
        val allQuestions = listOf(
            TestQuestion(1, "Я часто кричу на інших, коли злюсь"),
            TestQuestion(2, "Мені подобається дражнити інших"),
            TestQuestion(3, "Я можу навмисно ображати людей в інтернеті"),
            TestQuestion(4, "Я часто відчуваю роздратування"),
            TestQuestion(5, "Мені складно контролювати свій гнів"),
            TestQuestion(6, "Я іноді зриваю злість на близьких"),
            TestQuestion(7, "Я часто сварюся в коментарях у соцмережах"),
            TestQuestion(8, "Мене легко вивести з себе"),
            TestQuestion(9, "Я можу погрожувати іншим словами або діями"),
            TestQuestion(10, "Я часто підозрюю людей у поганих намірах"),
            TestQuestion(11, "Я ображаюсь через дрібниці"),
            TestQuestion(12, "Я іноді хочу зробити комусь боляче за образу"),
            TestQuestion(13, "Я часто втрачаю самоконтроль під час суперечок"),
            TestQuestion(14, "Я відчуваю задоволення, коли виграю сварку"),
            TestQuestion(15, "Я можу ображати інших, щоб захистити себе"),
            TestQuestion(16, "Я схильний відповідати агресією на провокації"),
            TestQuestion(17, "Я легко дратуюсь через поганий настрій"),
            TestQuestion(18, "Я можу підвищити голос у відповідь на критику"),
            TestQuestion(19, "Я часто сперечаюсь, навіть коли це не потрібно"),
            TestQuestion(20, "Я зневажаю людей, які поводяться слабко"),
            TestQuestion(21, "Я часто відчуваю, що мене недооцінюють"),
            TestQuestion(22, "Я зловтішаюся, коли хтось отримує по заслугах"),
            TestQuestion(23, "Я люблю провокувати інших на емоції"),
            TestQuestion(24, "Я часто відчуваю бажання довести свою правоту силою"),
            TestQuestion(25, "Я рідко стримую свої емоції у сварці"),
            TestQuestion(26, "Я використовую сарказм, щоб образити людину"),
            TestQuestion(27, "Я вважаю, що агресія — це нормальний спосіб самозахисту"),
            TestQuestion(28, "Я часто перебільшую образи, завдані мені іншими"),
            TestQuestion(29, "Я можу відреагувати фізично на образу"),
            TestQuestion(30, "Я іноді почуваюся задоволеним після конфлікту"),
            TestQuestion(31, "Я частіше шукаю сварку, ніж намагаюсь уникнути її"),
            TestQuestion(32, "Я іноді провокую конфлікти заради розваги"),
            TestQuestion(33, "Я легко втрачаю терпіння в онлайн-спілкуванні"),
            TestQuestion(34, "Я люблю показувати свою перевагу над іншими"),
            TestQuestion(35, "Я часто використовую жорсткі слова під час суперечок"),
            TestQuestion(36, "Я схильний перебільшувати негативні якості інших"),
            TestQuestion(37, "Я не шкодую, якщо мої слова зачепили когось"),
            TestQuestion(38, "Я відчуваю агресію, коли хтось мене ігнорує"),
            TestQuestion(39, "Я вважаю, що агресія виправдана в деяких випадках"),
            TestQuestion(40, "Я часто злюсь через дрібниці в мережі або реальному житті")
        )

        questions.addAll(allQuestions.shuffled().take(5))
    }

    private fun setupQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        binding.questionText.text = currentQuestion.text
        binding.answerGroup.clearCheck()
        binding.progressText.text = "${currentQuestionIndex + 1}/${questions.size}"
    }

    private fun saveAnswer() {
        val selectedId = binding.answerGroup.checkedRadioButtonId
        if (selectedId != -1) {
            val answerIndex = when (selectedId) {
                binding.answer1.id -> 0
                binding.answer2.id -> 1
                binding.answer3.id -> 2
                binding.answer4.id -> 3
                binding.answer5.id -> 4
                else -> 0
            }
            userAnswers[questions[currentQuestionIndex].id] = answerIndex + 1
        }
    }

    private fun showResult() {
        val totalScore = userAnswers.values.sum()
        val maxScore = questions.size * 5
        val percentage = (totalScore * 100) / maxScore

        val resultText = when {
            percentage < 30 -> "Низький рівень агресивності (${percentage}%)"
            percentage < 60 -> "Середній рівень (${percentage}%)"
            else -> "Високий рівень (${percentage}%)"
        }

        AlertDialog.Builder(this)
            .setTitle("Ваш результат")
            .setMessage(resultText)
            .setPositiveButton("OK") { _, _ -> finish() }
            .show()
    }
}