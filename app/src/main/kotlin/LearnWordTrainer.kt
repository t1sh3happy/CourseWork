import java.io.File

data class Statistics(
    val learnedCount: Int,
    val total: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordTrainer {

    private var question: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val learnedCount = dictionary.filter { it.correctAnswersCount >= 3 }.size
        val total = dictionary.size
        return Statistics(learnedCount, total)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < 3 }
        if (notLearnedList.isEmpty()) return null
        val correctAnswer = notLearnedList.random()
        val distractors = (dictionary - correctAnswer).shuffled().take(NUMBER_OF_ANSWERS - 1)
        val questionWords = (listOf(correctAnswer) + distractors).shuffled()
        question = Question(variants = questionWords, correctAnswer = correctAnswer)
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
        val wordsFile: File = File("words.txt")
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
    }

    private fun saveDictionary(dictionary: List<Word>) {
        val wordsFile: File = File("words.txt")
        val content =
            dictionary.joinToString(separator = "\n")
            { word -> "${word.text}|${word.translate}|${word.correctAnswersCount}" }
        wordsFile.writeText(content)
    }
}
