
fun main(args: Array<String>) {

    val botToken: String = args[0]
    var updateId = 0
    val updateIdRegex = Regex(""""update_id":\s*(\d+)""")
    val messageRegex = Regex("\"text\":\"(.+?)\"")
    val chatIdRegex = Regex(""""chat":\{"id":\s*(\d+)""")
    val dataRegex = Regex("\"data\":\"(.+?)\"")
    val learnWordTrainer = LearnWordTrainer()
    val telegramBotService = TelegramBotService(botToken)

    while (true) {

        Thread.sleep(2000)
        val updates: String = telegramBotService.getUpdates(botToken, updateId)
        val searchebleId = updateIdRegex.find(updates)?.groups?.get(1)?.value ?: continue
        val message = messageRegex.find(updates)?.groups?.get(1)?.value ?: continue
        val chatId = chatIdRegex.find(updates)?.groups?.get(1)?.value?.toLong() ?: continue
        val data = dataRegex.find(updates)?.groups?.get(1)?.value
        updateId = searchebleId.toInt() + 1
        var text: String
        println(searchebleId)
        println(updates)
        println(chatId)

        if (message == "Hello") {
            text = "Hello"
            telegramBotService.sendMessage(botToken, chatId, text)
        }
        if (message == "/start" && chatId != null) {
            telegramBotService.sendMenu(botToken, chatId)
        }
        if (data?.lowercase() == CALLBACK_LEARN_WORDS && chatId != null) {
            telegramBotService.sendMessage(botToken, chatId, "Учимся")
        }
        if (data?.lowercase() == CALLBACK_STATISTICS && chatId != null) {
            telegramBotService.sendMessage(botToken, chatId, "Cnfnbcnbrf")

        }
    }
}
