package no.nav.pensjon.vtp.client.support

import okhttp3.HttpUrl
import okhttp3.Request
import java.util.*

fun Request.Builder.basicAuth(username: String, password: String) = this.header(
    "Authorization",
    "Basic ${Base64.getEncoder().encodeToString(("$username:$password").toByteArray())}"
)

fun Request.Builder.url(url: String, queryParameters: Map<String, String>) = this.url(
    HttpUrl.get(url).newBuilder().run {
        queryParameters.forEach { (key, value) ->
            addQueryParameter(key, value)
        }
        build()
    }
)
