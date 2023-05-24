package no.nav.pensjon.vtp.auth

import com.nimbusds.oauth2.sdk.ParseException
import com.nimbusds.oauth2.sdk.auth.PrivateKeyJWT
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.Base64.getDecoder

fun getUser(authorization: String?): String? =
    if (authorization?.lowercase(Locale.getDefault())?.startsWith("basic") == true) {
        String(getDecoder().decode(authorization.substring("Basic".length).trim()), StandardCharsets.UTF_8).split(":")
            .firstOrNull()
    } else {
        null
    }

fun getClientId(clientAssertion: String?, clientAssertionType: String?): String? = try {
    PrivateKeyJWT.parse("client_assertion_type=$clientAssertionType&client_assertion=$clientAssertion").clientID.value
} catch (_: ParseException) {
    null
}