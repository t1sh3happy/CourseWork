import java.io.File

const val ERROR_CONDITION = -1

fun main() {

//    val wordsFile: File = File("words.txt")
//    wordsFile.createNewFile()
//    wordsFile.writeText("hello привет\n")
//    wordsFile.appendText("dog собака\n")
//    wordsFile.appendText("cat кошка\n")
//    val dictionary = mutableListOf<Word>()
//    val lines: List<String> = wordsFile.readLines()
//
//    for (line in lines) {
//        val parts = line.split("|")
//        val word =
//            Word(text = parts[0], translate = parts[1], correctAnswersCount = parts.getOrNull(2)?.toIntOrNull() ?: 0)
//        dictionary.add(word)
//    }
//    println(dictionary)
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
                break
            }

            1 -> println("Учить слова")
            2 -> println("Статистика")
            else -> println("Неверный ввод")
        }
    }
}

//data class Word(
//    val text: String,
//    val translate: String,
//    var correctAnswersCount: Int = 0,
//)