const val ERROR_CONDITION = -1
const val NUMBER_OF_ANSWERS = 4


fun Question.asConsoleString(): String {
    val variants = this.variants
        .mapIndexed { index: Int, word: Word -> "${index + 1}. ${word.translate}" }
        .joinToString(separator = "\n")
    return "${this.correctAnswer.text}:\n$variants\n----------\n0 - Меню"
}

fun main() {

    val trainer = try {
        LearnWordTrainer()
    } catch (e: Exception) {
        println("Невозможно загрузить словарь")
        return
    }

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
                while (true) {
                    val question = trainer.getNextQuestion()
                    if (question == null) {
                        println("Все слова выучены")
                        break
                    } else {
                        println(question.asConsoleString())

                        val userAnswerInput = readln().toIntOrNull() ?: ERROR_CONDITION

                        if (userAnswerInput == 0) break

                        if (trainer.checkAnswer(userAnswerInput.minus(1))) {
                            println("Правильно!")
                        } else {
                            println("Неправильно! ${question.correctAnswer.text} - это ${question.correctAnswer.translate}")
                        }
                    }
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
