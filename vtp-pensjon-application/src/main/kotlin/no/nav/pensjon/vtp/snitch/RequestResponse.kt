package no.nav.pensjon.vtp.snitch

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID.randomUUID

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
    val handlerClass: String?,
    val handlerMethod: String?,
    val exception: String?,
    val stackTrace: String?,

    val request: Payload,

    val response: Payload
)
