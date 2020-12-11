package no.nav.pensjon.vtp.core.util

import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.xml.datatype.DatatypeConfigurationException
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

class DateUtils

private val logger = LoggerFactory.getLogger(DateUtils::class.java)

internal fun datetypeFactoryFun(): DatatypeFactory {
    try {
        return DatatypeFactory.newInstance()
    } catch (e: DatatypeConfigurationException) {
        logger.error("Unable to create datetypefactory", e)
        throw RuntimeException(e)
    }
}

private val datatypeFactory = datetypeFactoryFun()

fun LocalDate.asXMLGregorianCalendar(): XMLGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(atStartOfDay(ZoneId.systemDefault())))

fun Calendar.toLocalDate(): LocalDate? {
    return LocalDateTime.ofInstant(this.toInstant(), this.timeZone.toZoneId()).toLocalDate()
}
