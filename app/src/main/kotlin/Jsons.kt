import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TelegramResponse(
    val ok: Boolean,
    val result: List<Update>
)

@Serializable
data class Update(
    val update_id: Long,
    val message: Message? = null,
    val callback_query: CallbackQuery? = null
)

@Serializable
data class Message(
    val message_id: Long,
    val chat: Chat,
    val text: String? = null
)

@Serializable
data class Chat(
    val id: Long
)

@Serializable
data class CallbackQuery(
    val id: String,
    val data: String? = null,
    val message: Message? = null
)