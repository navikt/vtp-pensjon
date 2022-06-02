package no.nav.pensjon.vtp.snitch

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID.randomUUID

@Document
data class RequestResponse(
    @Id
    val id: String = randomUUID().toString(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val method: String,
    val path: String,
    val queryString: String? = null,
    val url: String,
    val status: Int,
    val handler: String? = null,
    val handlerClass: String? = null,
    val handlerMethod: String? = null,
    val exception: String? = null,
    val stackTrace: String? = null,

    val request: Payload,

    val response: Payload
)
