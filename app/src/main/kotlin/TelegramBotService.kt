import java.awt.SystemColor.text
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"

class TelegramBotService(private val botToken: String) {


    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates: String = TELEGRAM_BASE_URL + botToken + "/getUpdates?offset=$updateId"
        val client1: HttpClient = HttpClient.newBuilder().build()
        val request1: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response1: HttpResponse<String> = client1.send(request1, HttpResponse.BodyHandlers.ofString())
        return response1.body()
    }

    fun sendMessage(botToken: String, chatId: Long, text: String) {
        val urlSendMessage: String =
            TELEGRAM_BASE_URL + botToken + "/sendMessage?chat_id=$chatId&text=${URLEncoder.encode(text, "UTF-8")}"
        val client2: HttpClient = HttpClient.newBuilder().build()
        val request2: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        val response2: HttpResponse<String> = client2.send(request2, HttpResponse.BodyHandlers.ofString())
    }

    fun sendMenu(botToken: String, chatId: Long): String {
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
                               "callback_data": "learning_click"
                        },
                        {
                            "text": "Статистика",
                            "callback_data": "statistic_click"
                        }
                        ]
                    ]
                }
             }
        """.trimIndent()
        val client: HttpClient = HttpClient.newBuilder().build()
        val request3: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()
        val response3: HttpResponse<String> = client.send(request3, HttpResponse.BodyHandlers.ofString())
        return response3.body()
    }


}