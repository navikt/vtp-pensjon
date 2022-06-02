package no.nav.pensjon.vtp.util

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

fun <T> T?.asResponseEntity(): ResponseEntity<T> = this
    ?.let(::ok)
    ?: notFound().build()

fun URI.withoutQueryParameters() = UriComponentsBuilder.fromUri(this).query(null).build().toUri()
