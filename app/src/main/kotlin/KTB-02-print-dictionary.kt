const val ERROR_CONDITION = -1
const val NUMBER_OF_ANSWERS = 4

data class Word(
    val text: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

fun Question.asConsoleString(): String {
    val variants = this.variants
        .mapIndexed { index: Int, word: Word -> "${index + 1}. ${word.translate}" }
        .joinToString(separator = "\n")
    return "${this.correctAnswer.text}:\n$variants\n----------\n0 - Меню"
}

fun main() {
    val trainer = LearnWordTrainer()

    while (true) {
        println(
            "Меню: \n" +
                    "1 – Учить слова\n" +
                    "2 – Статистика\n" +
                    "0 – Выход\n" +
                    "Введите цифру: "
        )
        when (readln().toIntOrNull() ?: ERROR_CONDITION) {
            0 -> {
                println("Завершение работы")
                return
            }

            1 -> {
                val question = trainer.getNextQuestion()
                if (question == null) {
                    println("Все слова выучены")
                    continue
                } else {
                    println(question.asConsoleString())

                    val userAnswerInput = readln().toIntOrNull() ?: ERROR_CONDITION
                    if (userAnswerInput == 0) continue

                    if (trainer.checkAnswer(userAnswerInput.minus(1))) {
                        println("Правильно!")
                    } else println("Неправильно! ${question.correctAnswer.text} - это ${question.correctAnswer.translate}")
                }
            }

            2 -> {
                val statistics = trainer.getStatistics()

                if (statistics.total == 0) {
                    println("Словарь пуст, возврат в меню")
                } else {
                    println(
                        "Выучено ${statistics.learnedCount} из ${statistics.total} слов |" +
                                " ${statistics.learnedCount * 100 / statistics.total} %"
                    )
                }
            }
            else -> println("Введите число 1, 2 или 0")

        }
    }
}
