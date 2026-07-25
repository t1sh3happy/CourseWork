import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"

fun main(args: Array<String>) {

    val botToken: String = args[0]
    val urlGetMe: String = TELEGRAM_BASE_URL + botToken + "/getMe"
    val urlGetUpdates: String = TELEGRAM_BASE_URL + botToken + "/getUpdates"

    val client0: HttpClient = HttpClient.newBuilder().build()
    val request0: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val response0: HttpResponse<String> = client0.send(request0, HttpResponse.BodyHandlers.ofString())
    println(response0.body())

    val client1: HttpClient = HttpClient.newBuilder().build()
    val request1: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response1: HttpResponse<String> = client1.send(request1, HttpResponse.BodyHandlers.ofString())
    println(response1.body())

}
