import java.io.File

data class Statistics(
    val total: Int,
    val learnedCount: Int,
    val percent : Int,
)

data class Word(
    val text: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordTrainer(private val learnedAnswerCount: Int = 3) {

    private var question: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val learnedCount = dictionary.filter { it.correctAnswersCount >= learnedAnswerCount }.size
        val total = dictionary.size
        val percent = if (learnedCount > 0) learnedCount * 100 / total else 0
        return Statistics(learnedCount, total, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < learnedAnswerCount }
        if (notLearnedList.isEmpty()) return null
        val correctAnswer = notLearnedList.random()
        val otherNotLearned = notLearnedList - correctAnswer
        val variants = if (otherNotLearned.size >= NUMBER_OF_ANSWERS - 1) {
            (listOf(correctAnswer) + otherNotLearned.shuffled().take(NUMBER_OF_ANSWERS - 1)).shuffled()
        } else {
            val learnedExtra = (dictionary - notLearnedList
                .toSet())
                .shuffled()
                .take(NUMBER_OF_ANSWERS - 1 - otherNotLearned.size)

            (listOf(correctAnswer) + otherNotLearned + learnedExtra).shuffled()
        }

        question = Question(variants = variants, correctAnswer = correctAnswer)
        return question

    }

    fun checkAnswer(userAnswersIndex: Int?): Boolean {

        return question?.let {
            val correctAnswerId = it.variants.indexOf(it.correctAnswer)
            if (correctAnswerId == userAnswersIndex) {
                it.correctAnswer.correctAnswersCount++
                saveDictionary(dictionary)
                true
            } else {
                false
            }
        } ?: false
    }

    private fun loadDictionary(): MutableList<Word> {
        try {
            val wordsFile = File("words.txt")
            val dictionary = mutableListOf<Word>()
            val lines: List<String> = wordsFile.readLines()

            for (line in lines) {
                val parts = line.split("|")
                val word =
                    Word(
                        text = parts[0],
                        translate = parts[1],
                        correctAnswersCount = parts.getOrNull(2)?.toIntOrNull() ?: 0
                    )
                dictionary.add(word)
            }
            return dictionary
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("Некорректный файл")
        }

    }

    private fun saveDictionary(dictionary: List<Word>) {
        val wordsFile: File = File("words.txt")
        val content =
            dictionary.joinToString(separator = "\n")
            { word -> "${word.text}|${word.translate}|${word.correctAnswersCount}" }
        wordsFile.writeText(content)
    }
}
