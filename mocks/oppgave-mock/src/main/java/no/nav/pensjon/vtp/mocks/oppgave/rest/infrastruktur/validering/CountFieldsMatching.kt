package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import org.springframework.beans.PropertyAccessorFactory

fun countFieldsMatching(any: Any, fields: Array<String>): Int {
    val beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(any)

    return fields.toList()
        .map { beanWrapper.getPropertyValue(it) }
        .filter { isNotBlank(it) }
        .count()
}

private fun isNotBlank(any: Any?): Boolean {
    return !isBlank(any)
}

private fun isBlank(any: Any?): Boolean {
    return any == null || any.toString().chars()
        .allMatch { codePoint: Int -> Character.isWhitespace(codePoint) }
}
