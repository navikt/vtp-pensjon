package no.nav.pensjon.vtp.util

import org.slf4j.MDC.put
import org.slf4j.MDC.remove

const val CORRELATION_ID_HEADER_NAME = "x-vtp-correlation-id"

inline fun withCorrelationId(correlationId: String, block: (correlationId: String) -> Unit) {
    withMdc(*correlationIdKeys.map { Pair(it, correlationId) }.toTypedArray()) {
        block(correlationId)
    }
}

fun setCorrelationId(correlationId: String) {
    correlationIdKeys.forEach { put(it, correlationId) }
}

fun resetCorrelationId() {
    correlationIdKeys.forEach { remove(it) }
}
