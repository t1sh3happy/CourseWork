import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    val botToken: String = args[0]
    var updateId: Long = 0

    val json = Json { ignoreUnknownKeys = true }
    val telegramBotService = TelegramBotService(botToken)

    val trainer = try {
        LearnWordTrainer()
    } catch (e: Exception) {
        println("Невозможно загрузить словарь")
        return
    }

    val questionByChatId = mutableMapOf<Long, Question>()

    while (true) {
        Thread.sleep(2000)
        val updates: String = telegramBotService.getUpdates(updateId)

        val response = try {
            json.decodeFromString<TelegramResponse>(updates)
        } catch (e: Exception) {
            println("Ошибка парсинга JSON: ${e.message}")
            continue
        }

        if (response.result.isEmpty()) continue

        for (update in response.result) {
            updateId = update.update_id + 1

            val chatId = update.message?.chat?.id
                ?: update.callback_query?.message?.chat?.id
                ?: continue

            val message = update.message?.text
            val data = update.callback_query?.data

            println("update_id=${update.update_id}, chatId=$chatId, message=$message, data=$data")

            if (message == "Hello") {
                telegramBotService.sendMessage(chatId, "Hello")
            }

            if (message == "/start") {
                telegramBotService.sendMenu(chatId)
            }

            when {
                data == CLICKED_LEARN_WORDS -> {
                    val question = trainer.getNextQuestion()
                    if (question == null) {
                        telegramBotService.sendMessage(chatId, "Все слова в словаре выучены")
                    } else {
                        questionByChatId[chatId] = question
                        telegramBotService.sendQuestion(chatId, question)
                    }
                }

                data == CLICKED_STATISTICS -> {
                    val statistics = trainer.getStatistics()
                    if (statistics.totalCount == 0) {
                        telegramBotService.sendMessage(chatId, "Словарь пуст, возврат в меню")
                    } else {
                        telegramBotService.sendMessage(
                            chatId,
                            "Выучено ${statistics.learnedCount} из ${statistics.totalCount} слов | " +
                                    "${statistics.percent} %"
                        )
                    }
                }

                data?.startsWith(CALLBACK_DATA_ANSWER_PREFIX) == true -> {
                    val userAnswerIndex = data.substringAfter(CALLBACK_DATA_ANSWER_PREFIX).toIntOrNull()

                    if (userAnswerIndex == null) {
                        telegramBotService.sendMessage(chatId, "Некорректный ответ")
                        continue
                    }

                    val question = questionByChatId[chatId]

                    if (question == null) {
                        telegramBotService.sendMessage(
                            chatId,
                            "Вопрос устарел или не найден. Нажмите «Изучить слова»."
                        )
                        continue
                    }

                    val isCorrect = trainer.checkAnswer(question, userAnswerIndex)

                    if (isCorrect) {
                        telegramBotService.sendMessage(chatId, "Правильно!")
                    } else {
                        val hint = trainer.getCurrentQuestionHint(question)
                        telegramBotService.sendMessage(chatId, "Неправильно! $hint")
                    }

                    questionByChatId.remove(chatId)

                    val nextQuestion = trainer.getNextQuestion()
                    if (nextQuestion == null) {
                        telegramBotService.sendMessage(chatId, "Все слова в словаре выучены")
                    } else {
                        questionByChatId[chatId] = nextQuestion
                        telegramBotService.sendQuestion(chatId, nextQuestion)
                    }
                }
            }
        }
    }
}
