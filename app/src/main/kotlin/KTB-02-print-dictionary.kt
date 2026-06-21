import java.io.File


fun main() {

    val wordsFile: File = File("words.txt")
//    wordsFile.createNewFile()
//    wordsFile.writeText("hello привет\n")
//    wordsFile.appendText("dog собака\n")
//    wordsFile.appendText("cat кошка\n")
    val dictionary = mutableListOf<Word>()
    val lines: List<String> = wordsFile.readLines()

    for (line in lines) {
        val line = line.split("|")
        val word = Word(text = line[0], traslate = line[1], correctAnswersCount = line.getOrNull(2)?.toIntOrNull() ?: 0)
        dictionary.add(word)
    }
    println(dictionary)
}

data class Word(
    val text: String,
    val traslate: String,
    var correctAnswersCount: Int = 0,
)