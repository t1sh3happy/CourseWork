import java.io.File


const val ERROR_CONDITION = -1
const val NUMBER_OF_ANSWERS = 4

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

            1 -> {
                if (dictionary.filter { it.correctAnswersCount < 3 }.isEmpty()) {
                    println("Все слова выучены")
                    continue
                } else {
                    val notLearnedList = dictionary.filter { it.correctAnswersCount < 3 }
                    val questionWords: List<Word>
                    if (notLearnedList.size >= NUMBER_OF_ANSWERS) {
                        questionWords = notLearnedList.shuffled().take(NUMBER_OF_ANSWERS)
                    } else {
                        val extra = (dictionary - notLearnedList.toList()).shuffled()
                            .take(NUMBER_OF_ANSWERS - notLearnedList.size)
                        questionWords = (notLearnedList + extra).shuffled()
                    }

                    val correctAnswer = notLearnedList.random()
                    println("${correctAnswer.text}:")
                    println(
                        "1 - ${questionWords[0].translate} " +
                                "2 - ${questionWords[1].translate} " +
                                "3 - ${questionWords[2].translate} " +
                                "4 - ${questionWords[3].translate} "
                    )
                    var answer = readln().toIntOrNull() ?: ERROR_CONDITION
                    while (answer != questionWords.indexOf(correctAnswer) + 1) {
                        println("Вы ошиблись, повторите попытку")
                        answer = readln().toIntOrNull() ?: ERROR_CONDITION
                    }
                    println("Вы угадали слово")
                }

            }

            2 -> {
                val learnedCount = dictionary.filter { it.correctAnswersCount >= 3 }.size
                val total = dictionary.size
                if (total == 0) {
                    println("Словарь пуст, возврат в меню")

                } else {
                    println("Выучено $learnedCount из $total слов | ${learnedCount * 100 / total} %")
                }
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
