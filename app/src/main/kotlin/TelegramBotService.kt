import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.URLEncoder

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"


fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates: String = TELEGRAM_BASE_URL + botToken + "/getUpdates?offset=$updateId"
    val client1: HttpClient = HttpClient.newBuilder().build()
    val request1: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response1: HttpResponse<String> = client1.send(request1, HttpResponse.BodyHandlers.ofString())
    return response1.body()
}

fun sendMessage(botToken: String, chatId: Int, text: String) {
    val urlSendMessage: String = TELEGRAM_BASE_URL + botToken + "/sendMessage?chat_id=$chatId&text=${URLEncoder.encode(text, "UTF-8")}"
    val client2: HttpClient = HttpClient.newBuilder().build()
    val request2: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
    val response2: HttpResponse<String> = client2.send(request2, HttpResponse.BodyHandlers.ofString())
}

