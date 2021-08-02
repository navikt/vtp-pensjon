package no.nav.pensjon.vtp.auth

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
