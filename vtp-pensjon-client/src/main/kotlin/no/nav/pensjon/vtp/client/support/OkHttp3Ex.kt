package no.nav.pensjon.vtp.client.support

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import java.util.*

fun Request.Builder.basicAuth(username: String, password: String) = this.header(
    "Authorization",
    "Basic ${Base64.getEncoder().encodeToString(("$username:$password").toByteArray())}"
)

fun Request.Builder.url(url: String, queryParameters: Map<String, String>) = this.url(
    url.toHttpUrl().newBuilder().run {
        queryParameters.forEach { (key, value) ->
            addQueryParameter(key, value)
        }
        build()
    }
)

val APPLICATION_JSON = "application/json".toMediaTypeOrNull()
