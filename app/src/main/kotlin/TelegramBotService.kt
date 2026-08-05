import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.json.Json

const val CALLBACK_LEARN_WORDS = "learning_click"
const val CALLBACK_STATISTICS = "statistic_click"
const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"



class TelegramBotService(private val botToken: String) {




    private val client: HttpClient = HttpClient.newBuilder().build()
    private val json = Json { ignoreUnknownKeys = true }

    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates: String = TELEGRAM_BASE_URL + botToken + "/getUpdates?offset=$updateId"
        val request1: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response1: HttpResponse<String> = client.send(request1, HttpResponse.BodyHandlers.ofString())
        return response1.body()
    }

    fun sendMessage(botToken: String, chatId: Long, text: String) {
        val urlSendMessage: String =
            TELEGRAM_BASE_URL + botToken + "/sendMessage?chat_id=$chatId&text=${URLEncoder.encode(text, "UTF-8")}"
        val request2: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        client.send(request2, HttpResponse.BodyHandlers.ofString())
    }

    fun sendMenu(botToken: String, chatId: Long){
        val urlSendMessage: String = TELEGRAM_BASE_URL + botToken + "/sendMessage"
        val sendMenuBody = """
             {
                "chat_id": $chatId,
                "text": "Основное меню",
                "reply_markup": {
                    "inline_keyboard": [
                        [
                        {
                           "text": "Изучить слова",
                           "callback_data": "$CALLBACK_LEARN_WORDS"
                        },
                        {
                           "text": "Статистика",
                           "callback_data": "$CALLBACK_STATISTICS"
                        }
                        ]
                    ]
                }
             }
        """.trimIndent()
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
        }
}