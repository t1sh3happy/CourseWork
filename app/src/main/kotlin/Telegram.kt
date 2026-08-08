fun main(args: Array<String>) {

    val botToken: String = args[0]
    var updateId = 0
    val updateIdRegex = Regex(""""update_id":\s*(\d+)""")
    val messageRegex = Regex("\"text\":\"(.+?)\"")
    val chatIdRegex = Regex(""""chat":\{"id":\s*(\d+)""")
    val dataRegex = Regex("\"data\":\"(.+?)\"")
    val telegramBotService = TelegramBotService(botToken)
    val trainer = try {
        LearnWordTrainer()
    } catch (e: Exception) {
        println("Невозможно загрузить словарь")
        return
    }
    val statistics = trainer.getStatistics()
    val question = trainer.getNextQuestion()



    while (true) {

        Thread.sleep(2000)
        val updates: String = telegramBotService.getUpdates(updateId)
        val searchebleId = updateIdRegex.find(updates)?.groups?.get(1)?.value ?: continue
        updateId = searchebleId.toInt() + 1
        val chatId = chatIdRegex.find(updates)?.groups?.get(1)?.value?.toLong() ?: continue
        val message = messageRegex.find(updates)?.groups?.get(1)?.value
        val data = dataRegex.find(updates)?.groups?.get(1)?.value
        var text: String

        println(updates)

        if (message == "Hello") {
            text = "Hello"
            telegramBotService.sendMessage(chatId, text)
        }

        if (message == "/start" && chatId != null) {
            telegramBotService.sendMenu(chatId)
        }

        when (data?.lowercase()) {

            CLICKED_LEARN_WORDS -> {
                trainer.checkNextQuestionAndSend(trainer, telegramBotService, chatId)


            }

            CLICKED_STATISTICS -> {

                if (statistics.totalCount == 0) {
                    telegramBotService.sendMessage(chatId, "Словарь пуст, возврат в меню")
                } else {
                    telegramBotService.sendMessage(
                        chatId, "Выучено ${statistics.learnedCount} из ${statistics.totalCount} слов | " +
                                "${statistics.percent} %"
                    )
                }

            }
        }
    }

}
