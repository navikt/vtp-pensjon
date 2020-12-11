package no.nav.pensjon.vtp.snitch

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID.randomUUID

data class Payload(
    val headers: Map<String, List<String>>,
    val contentType: String?,
    val contentLength: Int?,
    val content: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Payload

        if (headers != other.headers) return false
        if (contentType != other.contentType) return false
        if (contentLength != other.contentLength) return false

        return true
    }

    override fun hashCode(): Int {
        var result = headers.hashCode()
        result = 31 * result + (contentType?.hashCode() ?: 0)
        result = 31 * result + (contentLength ?: 0)
        return result
    }
}

@Document
data class RequestResponse(
    @Id
    val id: String = randomUUID().toString(),
    val timestamp: LocalDateTime,
    val method: String,
    val path: String,
    val url: String,
    val status: Int,
    val handler: String?,
    val exception: String?,
    val stackTrace: String?,

    val request: Payload,

    val response: Payload
)
