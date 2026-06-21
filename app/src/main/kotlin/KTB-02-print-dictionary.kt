import java.io.File
import kotlin.collections.filter

const val ERROR_CONDITION = -1

fun main() {

    val dictionary = loadDictionary()

    while (true) {
        println(
            "Меню: \n" +
                    "1 – Учить слова\n" +
                    "2 – Статистика\n" +
                    "0 – Выход\n" +
                    "Введите цифру: "
        )
        val state: Int = readln().toIntOrNull() ?: ERROR_CONDITION
        when (state) {
            0 -> {
                println("Завершение работы")
                return
            }

            1 -> println("Учить слова")
            2 -> {
                val learnedCount = dictionary.filter { it.correctAnswersCount >= 3 }.size
                var total = 0
                for (word in dictionary) {
                    total++
                }
                println("Общее количество слов $total")
                val percentage = learnedCount * 100 / total
                println("Выучено $learnedCount из $total слов | $percentage %")

            }

            else -> println("Введите число 1, 2 или 0")
        }
    }


}

data class Word(
    val text: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

fun loadDictionary(): MutableList<Word> {
    val wordsFile: File = File("words.txt")
    val dictionary = mutableListOf<Word>()
    val lines: List<String> = wordsFile.readLines()

    for (line in lines) {
        val parts = line.split("|")
        val word =
            Word(text = parts[0], translate = parts[1], correctAnswersCount = parts.getOrNull(2)?.toIntOrNull() ?: 0)
        dictionary.add(word)
    }
    return dictionary
}
