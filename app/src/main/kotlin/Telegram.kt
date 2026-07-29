

fun main(args: Array<String>) {

    val botToken: String = args[0]
    var updateId = 0
    val updateIdRegex = Regex(""""update_id":\s*(\d+)""")
    val messageRegex = Regex("\"text\":\"(.+?)\"")
    val chatIdRegex = Regex(""""chat":\{"id":\s*(\d+)""")


    while (true) {
        Thread.sleep(2000)
        val updates: String = getUpdates(botToken, updateId)
        val searchebleId = updateIdRegex.find(updates)?.groups?.get(1)?.value ?: continue
        val message = messageRegex.find(updates)?.groups?.get(1)?.value ?: continue
        val chatId = chatIdRegex.find(updates)?.groups?.get(1)?.value?.toInt() ?: continue
        val text: String
        println(searchebleId)
        println(updates)
        println(chatId)

        if (message == "Hello") {
            text = "Hello"
            sendMessage(botToken, chatId, text)
        }

        updateId = searchebleId.toInt() + 1

    }
}
