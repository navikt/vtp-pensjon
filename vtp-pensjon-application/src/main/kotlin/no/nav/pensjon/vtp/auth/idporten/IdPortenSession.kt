package no.nav.pensjon.vtp.auth.idporten

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class IdPortenSession(
    @Id
    val code: String = UUID.randomUUID().toString(),
    val nonce: String? = null,
    val pid: String
)
