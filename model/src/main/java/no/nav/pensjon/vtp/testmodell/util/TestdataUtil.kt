package no.nav.pensjon.vtp.testmodell.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

object TestdataUtil {
    @JvmStatic
    fun generateRandomPlausibleBirtdayParent(): LocalDate {
        val startRange = LocalDate.of(1960, 1, 1)
        val endRange = LocalDate.of(2000, 1, 1)
        val startEpoch = startRange.toEpochDay()
        val endEpoch = endRange.toEpochDay()
        val random = Random()
        val randomEpochDay = startEpoch + (random.nextDouble() * endEpoch - startEpoch).toLong()
        return LocalDate.ofEpochDay(randomEpochDay)
    }

    @JvmStatic
    fun generateBirtdayBetweenYears(start: Int, end: Int): LocalDate {
        val startRange = LocalDate.of(start, 1, 1)
        val endRange = LocalDate.of(end, 12, 31)
        val startEpoch = startRange.toEpochDay()
        val endEpoch = endRange.toEpochDay()
        val random = Random()
        val randomEpochDay = random.longs(startEpoch, endEpoch).findFirst().orElse(0L)
        return LocalDate.ofEpochDay(randomEpochDay)
    }

    @JvmStatic
    fun generateBirthdateNowMinusThreeYears(): LocalDate {
        val endRange = LocalDateTime.now().toLocalDate()
        val startRange = endRange.minus(3, ChronoUnit.YEARS)
        val startEpoch = startRange.toEpochDay()
        val endEpoch = endRange.toEpochDay()
        val random = Random()
        val randomEpochDay = startEpoch + (random.nextDouble() * (endEpoch - startEpoch)).toLong()
        return LocalDate.ofEpochDay(randomEpochDay)
    }

    fun generateBirthdayYoungerThanSixMonths(): LocalDate {
        //Note: this actually returns younger than five months, but ok since it's also younger than six months...
        val endRange = LocalDateTime.now().toLocalDate()
        val startRange = endRange.minus(5, ChronoUnit.MONTHS)
        val startEpoch = startRange.toEpochDay()
        val endEpoch = endRange.toEpochDay()
        val random = Random()
        val randomEpochDay = startEpoch + (random.nextDouble() * (endEpoch - startEpoch)).toLong()
        return LocalDate.ofEpochDay(randomEpochDay)
    }
}