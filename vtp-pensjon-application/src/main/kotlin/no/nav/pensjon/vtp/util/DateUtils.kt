package no.nav.pensjon.vtp.util

import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.ofInstant
import java.time.ZoneId.systemDefault
import java.util.*
import java.util.GregorianCalendar.from
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

fun LocalDate.asGregorianCalendar(): GregorianCalendar = from(atStartOfDay(systemDefault()))

fun LocalDate.asXMLGregorianCalendar(): XMLGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(asGregorianCalendar())

fun LocalDateTime.asXMLGregorianCalendar(): XMLGregorianCalendar =
    datatypeFactory.newXMLGregorianCalendar(
        GregorianCalendar().apply {
            time = Date.from(atZone(systemDefault()).toInstant())
        }
    )

fun Calendar.toLocalDate(): LocalDate = ofInstant(toInstant(), timeZone.toZoneId()).toLocalDate()

fun XMLGregorianCalendar.asLocalDate(): LocalDate = toGregorianCalendar().toZonedDateTime().toLocalDate()
