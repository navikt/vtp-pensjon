package no.nav.pensjon.vtp.core.util

import org.slf4j.MDC.put
import org.slf4j.MDC.remove

const val CORRELATION_ID: String = "correlation-id"
const val TRANSACTION: String = "transaction"

val correlationIdKeys = listOf(CORRELATION_ID, TRANSACTION)

inline fun withMdc(vararg pairs: Pair<String, String>, block: () -> Unit) {
    try {
        pairs.forEach { (key, value) -> put(key, value) }
        block()
    } finally {
        pairs.forEach { (key, _) -> remove(key) }
    }
}
