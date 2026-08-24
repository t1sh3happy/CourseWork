import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramResponse(
    val ok: Boolean,
    val result: List<Update>
)

@Serializable
data class Update(
    @SerialName("update_id") val updateId: Int,
    val message: Message? = null,
    @SerialName("callback_query") val callbackQuery: CallbackQuery? = null
)

@Serializable
data class Message(
    @SerialName("message_id") val messageId: Long,
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