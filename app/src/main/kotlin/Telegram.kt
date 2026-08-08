fun main(args: Array<String>) {

    val botToken: String = args[0]
    var updateId = 0
    val updateIdRegex = Regex(""""update_id":\s*(\d+)""")
    val messageRegex = Regex("\"text\":\"(.+?)\"")
    val chatIdRegex = Regex(""""chat":\{"id":\s*(\d+)""")
    val dataRegex = Regex("\"data\":\"(.+?)\"")
    val telegramBotService = TelegramBotService(botToken)

    while (true) {

        Thread.sleep(2000)
        val updates: String = telegramBotService.getUpdates(botToken, updateId)
        val searchebleId = updateIdRegex.find(updates)?.groups?.get(1)?.value ?: continue
        updateId = searchebleId.toInt() + 1
        val chatId = chatIdRegex.find(updates)?.groups?.get(1)?.value?.toLong() ?: continue
        val message = messageRegex.find(updates)?.groups?.get(1)?.value
        val data = dataRegex.find(updates)?.groups?.get(1)?.value
        updateId = searchebleId.toInt() + 1
        var text: String
        println(searchebleId)
        println(updates)
        println(chatId)

        val trainer = try {
            LearnWordTrainer()
        } catch (e: Exception) {
            println("Невозможно загрузить словарь")
            return
        }

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

            val statistics = trainer.getStatistics()

            if (statistics.total == 0) {
                telegramBotService.sendMessage(botToken, chatId, "Словарь пуст, возврат в меню")
            } else {
                telegramBotService.sendMessage(
                    botToken, chatId, "Выучено ${statistics.learnedCount} из ${statistics.total} слов | " +
                            "${statistics.learnedCount * 100 / statistics.total} %"
                )

            }

        }
    }
}
