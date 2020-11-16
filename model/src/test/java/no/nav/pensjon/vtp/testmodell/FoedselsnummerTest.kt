package no.nav.pensjon.vtp.testmodell

import no.nav.pensjon.vtp.testmodell.identer.FiktiveFnr
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class FoedselsnummerTest {
    @Test
    fun fiktiv_vilkaarlig_kjonn_lager_fnr() {
        val fiktiveFnr = FiktiveFnr()
        val fnr = fiktiveFnr.tilfeldigFnr()
        Assertions.assertThat(fnr).isNotEmpty()
        Assertions.assertThat(fnr).hasSize(11)
    }

    @Test
    fun fiktiv_fnr_kjonn_mann() {
        val fiktiveFnr = FiktiveFnr()
        val fnr = fiktiveFnr.tilfeldigMannFnr(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
        Assertions.assertThat(fnr).hasSize(11)
        Assert.assertTrue(fnr.substring(6, 9).toInt() % 2 == 1)
    }

    @Test
    fun fiktiv_fnr_kjonn_kvinne() {
        val fiktiveFnr = FiktiveFnr()
        val fnr = fiktiveFnr.tilfeldigKvinneFnr(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
        Assertions.assertThat(fnr).hasSize(11)
        Assert.assertTrue(fnr.substring(6, 9).toInt() % 2 == 0)
    }

    @Test
    fun fiktiv_dnr() {
        val fiktiveFnr = FiktiveFnr()
        val fnr = fiktiveFnr.tilfeldigKvinneDnr()
        Assertions.assertThat(fnr).hasSize(11)
        Assert.assertTrue(fnr.substring(0, 1).toInt() >= 4)
    }

    @Test
    fun fiktiv_fnr_barn() {
        val fiktiveFnr = FiktiveFnr()
        val fnr = fiktiveFnr.tilfeldigBarnUnderTreAarFnr()
        Assertions.assertThat(fnr).hasSize(11)
        val dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy")
        val barnetsFoedselsdag = LocalDate.parse(fnr.substring(0, 6), dateTimeFormatter)
        val yearDiff = barnetsFoedselsdag.until(LocalDate.now(), ChronoUnit.YEARS)
        Assertions.assertThat(yearDiff).isBetween(0L, 3L)
    }
}