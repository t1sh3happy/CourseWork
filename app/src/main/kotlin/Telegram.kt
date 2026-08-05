import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"

fun main(args: Array<String>) {

    val botToken: String = args[0]
    var updateId = 0
    val updateIdRegex = Regex(""""update_id":\s*(\d+)""")
    val messageRegex = Regex("\"text\":\"(.+?)\"")


    while (true) {
        Thread.sleep(2000)
        val updates : String = getUpdates(botToken, updateId)
        val searchebleId = updateIdRegex.find(updates)?.groups?.get(1)?.value ?: continue
        val message = messageRegex.find(updates)?.groups?.get(1)?.value ?: continue
        println(message)
        println(searchebleId)

        updateId = searchebleId.toInt() + 1


    }
}


fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates: String = TELEGRAM_BASE_URL + botToken + "/getUpdates?offset=$updateId"
    val client1: HttpClient = HttpClient.newBuilder().build()
    val request1: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response1: HttpResponse<String> = client1.send(request1, HttpResponse.BodyHandlers.ofString())
    return response1.body()
}



