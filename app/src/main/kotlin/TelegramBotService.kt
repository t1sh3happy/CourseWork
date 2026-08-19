import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val CLICKED_LEARN_WORDS = "learning_click"
const val CLICKED_STATISTICS = "statistic_click"
const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"
const val CALLBACK_DATA_ANSWER_PREFIX = "answer_"


@Serializable
data class SendMessageRequest(
    val chat_id: Long,
    val text: String,
    val reply_markup: ReplyMarkup? = null
)

@Serializable
data class ReplyMarkup(
    val inline_keyboard: List<List<InlineKeyboardButton>>
)

@Serializable
data class InlineKeyboardButton(
    val text: String,
    val callback_data: String
)

class TelegramBotService(private val botToken: String) {

    private val client: HttpClient = HttpClient.newBuilder().build()
    private val json = Json { ignoreUnknownKeys = true }

    fun getUpdates(updateId: Int): String {
        val url = "$TELEGRAM_BASE_URL$botToken/getUpdates?offset=$updateId"
        val request = HttpRequest.newBuilder().uri(URI.create(url)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(chatId: Long, text: String) {
        val url = "$TELEGRAM_BASE_URL$botToken/sendMessage"
        val body = SendMessageRequest(chat_id = chatId, text = text)
        val jsonBody = json.encodeToString(body)

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build()

        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun sendMenu(chatId: Long) {
        val url = "$TELEGRAM_BASE_URL$botToken/sendMessage"
        val buttons = listOf(
            listOf(
                InlineKeyboardButton("Изучить слова", CLICKED_LEARN_WORDS),
                InlineKeyboardButton("Статистика", CLICKED_STATISTICS)
            )
        )
        val body = SendMessageRequest(
            chat_id = chatId,
            text = "Основное меню",
            reply_markup = ReplyMarkup(buttons)
        )
        val jsonBody = json.encodeToString(body)

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build()

        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun sendQuestion(chatId: Long, question: Question) {
        val url = "$TELEGRAM_BASE_URL$botToken/sendMessage"
        val text = "Как переводится: ${question.correctAnswer.text}?"

        val buttons = question.variants.mapIndexed { index, word ->
            InlineKeyboardButton(
                text = word.translate,
                callback_data = "$CALLBACK_DATA_ANSWER_PREFIX$index"
            )
        }.chunked(2)

        val body = SendMessageRequest(
            chat_id = chatId,
            text = text,
            reply_markup = ReplyMarkup(buttons)
        )
        val jsonBody = json.encodeToString(body)

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build()

        client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}