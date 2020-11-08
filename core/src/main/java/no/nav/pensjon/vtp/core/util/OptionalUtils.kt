package no.nav.pensjon.vtp.core.util

import java.util.*

fun <T : Any> Optional<T>.toNullable(): T? = this.orElse(null)

fun <T : Any> T?.toOptional(): Optional<T> = Optional.ofNullable(this)
