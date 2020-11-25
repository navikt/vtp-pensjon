package no.nav.pensjon.vtp.snitch

import java.time.LocalDateTime

data class Payload(
    val headers: Map<String, List<String>>,
    val contentType: String?,
    val contentLength: Int?,
    val content: ByteArray?
)

data class RequestResponse(
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
