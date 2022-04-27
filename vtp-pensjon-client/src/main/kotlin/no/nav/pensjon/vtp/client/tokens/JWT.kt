package no.nav.pensjon.vtp.client.tokens

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

internal class JWT(val header: JsonNode, val payload: JsonNode) {
    val subject: String
        get() = getStringClaim("sub")

    fun getStringClaim(claim: String): String = getStringClaimOrNull(claim)
        ?: throw RuntimeException("JWT did not contain claim $claim")

    private fun getStringClaimOrNull(claim: String): String? = payload.get(claim)?.asText()

    companion object {
        internal fun decode(jwt: String): JWT = jwt.split(".").run {
            if (size != 2 && size != 3) {
                throw java.lang.IllegalArgumentException("Expected JWT to contain either two or three parts")
            }
            return JWT(
                header = objectMapper.readTree(Base64.getDecoder().decode(get(0))),
                payload = objectMapper.readTree(Base64.getDecoder().decode(get(1))),
            )
        }

        private val objectMapper = ObjectMapper()
    }
}
