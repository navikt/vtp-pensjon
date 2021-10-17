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

/**
 * Compares two date periods to see if they are overlapping.
 *
 * See the answer from the following stack overflow question for explanation.
 * https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap/325964.
 */
fun isOverlapping(startA: LocalDate?, endA: LocalDate?, startB: LocalDate?, endB: LocalDate?) =
    (endB == null || startA == null || startA <= endB) &&
        (endA == null || startB == null || endA >= startB)

fun isOverlapping(startA: Calendar?, endA: Calendar?, startB: Calendar?, endB: Calendar?) =
    isOverlapping(startA?.toLocalDate(), endA?.toLocalDate(), startB?.toLocalDate(), endB?.toLocalDate())
