
package no.nav.pensjon.vtp.mocks.oppdrag.okonomi.tilbakekrevingservice

import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagBelopDto
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagDto
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagPeriodeDto
import no.nav.tilbakekreving.kravgrunnlag.hentliste.v1.ReturnertKravgrunnlagDto
import no.nav.tilbakekreving.typer.v1.JaNeiDto
import no.nav.tilbakekreving.typer.v1.PeriodeDto
import no.nav.tilbakekreving.typer.v1.TypeGjelderDto
import no.nav.tilbakekreving.typer.v1.TypeKlasseDto
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate

private const val ENHET = "4815"

fun hentGrunnlag(): DetaljertKravgrunnlagDto {
    return DetaljertKravgrunnlagDto().apply {
        vedtakId = BigInteger.valueOf(207406)
        kravgrunnlagId = BigInteger.valueOf(152806)
        datoVedtakFagsystem = LocalDate.of(2019, 3, 14).asXMLGregorianCalendar()
        enhetAnsvarlig = ENHET
        fagsystemId = "10000000000000000"
        kodeFagomraade = "PENAP"
        kodeHjemmel = "22-15-1-1"
        kontrollfelt = "42354353453454"
        referanse = "1"
        renterBeregnes = JaNeiDto.N
        saksbehId = "Z991136"
        utbetalesTilId = "10127435540" // mock verdi
        enhetBehandl = ENHET
        enhetBosted = ENHET
        kodeStatusKrav = "BEHA"
        typeGjelderId = TypeGjelderDto.PERSON
        typeUtbetId = TypeGjelderDto.PERSON
        vedtakGjelderId = "10127435540" // mock verdi
        vedtakIdOmgjort = BigInteger.valueOf(207407)
        tilbakekrevingsPeriode.addAll(lagTilbakekrevingsPerioder())
    }
}

private fun lagTilbakekrevingsPerioder() = listOf(
    DetaljertKravgrunnlagPeriodeDto().apply {
        periode = PeriodeDto().apply {
            fom = LocalDate.of(2016, 3, 16).asXMLGregorianCalendar()
            tom = LocalDate.of(2016, 3, 25).asXMLGregorianCalendar()
        }
        tilbakekrevingsBelop.addAll(
            listOf(
                DetaljertKravgrunnlagBelopDto().apply {
                    typeKlasse = TypeKlasseDto.FEIL
                    belopNy = BigDecimal.valueOf(9000.00)
                    belopOpprUtbet = BigDecimal.ZERO
                    belopTilbakekreves = BigDecimal.ZERO
                    belopUinnkrevd = BigDecimal.ZERO
                    kodeKlasse = "KL_KODE_FEIL_PEN"
                    skattProsent = BigDecimal.valueOf(0)
                },
                DetaljertKravgrunnlagBelopDto().apply {
                    typeKlasse = TypeKlasseDto.YTEL
                    belopNy = BigDecimal.ZERO
                    belopOpprUtbet = BigDecimal.valueOf(6000.00)
                    belopTilbakekreves = BigDecimal.valueOf(6000.00)
                    belopUinnkrevd = BigDecimal.ZERO
                    kodeKlasse = "PENAPGP"
                    skattProsent = BigDecimal.valueOf(30)
                }
            )
        )
        belopSkattMnd = BigDecimal(2700)
    },

    DetaljertKravgrunnlagPeriodeDto().apply {
        periode = PeriodeDto().apply {
            fom = LocalDate.of(2016, 3, 26).asXMLGregorianCalendar()
            tom = LocalDate.of(2016, 3, 31).asXMLGregorianCalendar()
        }
        tilbakekrevingsBelop.add(
            DetaljertKravgrunnlagBelopDto().apply {
                typeKlasse = TypeKlasseDto.YTEL
                belopNy = BigDecimal.ZERO
                belopOpprUtbet = BigDecimal.valueOf(3000.00)
                belopTilbakekreves = BigDecimal.valueOf(3000.00)
                belopUinnkrevd = BigDecimal.ZERO
                kodeKlasse = "PENAPGP"
                skattProsent = BigDecimal.valueOf(30)
            }
        )
        belopSkattMnd = BigDecimal(2700)
    },

    DetaljertKravgrunnlagPeriodeDto().apply {
        periode = PeriodeDto().apply {
            fom = LocalDate.of(2016, 4, 1).asXMLGregorianCalendar()
            tom = LocalDate.of(2016, 4, 30).asXMLGregorianCalendar()
        }

        tilbakekrevingsBelop.addAll(
            listOf(
                DetaljertKravgrunnlagBelopDto().apply {
                    typeKlasse = TypeKlasseDto.FEIL
                    belopNy = BigDecimal.valueOf(21000.00)
                    belopOpprUtbet = BigDecimal.ZERO
                    belopTilbakekreves = BigDecimal.ZERO
                    belopUinnkrevd = BigDecimal.ZERO
                    kodeKlasse = "KL_KODE_FEIL_PEN"
                    skattProsent = BigDecimal.valueOf(0)
                },
                DetaljertKravgrunnlagBelopDto().apply {
                    typeKlasse = TypeKlasseDto.YTEL
                    belopNy = BigDecimal.ZERO
                    belopOpprUtbet = BigDecimal.valueOf(21000.00)
                    belopTilbakekreves = BigDecimal.valueOf(21000.00)
                    belopUinnkrevd = BigDecimal.ZERO
                    kodeKlasse = "PENAPGA"
                    skattProsent = BigDecimal.valueOf(25.456)
                }
            )
        )

        belopSkattMnd = BigDecimal(5346)
    },

    DetaljertKravgrunnlagPeriodeDto().apply {
        periode = PeriodeDto().apply {
            fom = LocalDate.of(2016, 5, 1).asXMLGregorianCalendar()
            tom = LocalDate.of(2016, 5, 26).asXMLGregorianCalendar()
        }
        tilbakekrevingsBelop.addAll(
            listOf(
                DetaljertKravgrunnlagBelopDto().apply {
                    typeKlasse = TypeKlasseDto.FEIL
                    belopNy = BigDecimal.valueOf(9000.00)
                    belopOpprUtbet = BigDecimal.ZERO
                    belopTilbakekreves = BigDecimal.ZERO
                    belopUinnkrevd = BigDecimal.ZERO
                    kodeKlasse = "KL_KODE_FEIL_PEN"
                    skattProsent = BigDecimal.valueOf(0)
                },
                DetaljertKravgrunnlagBelopDto().apply {
                    typeKlasse = TypeKlasseDto.YTEL
                    belopNy = BigDecimal.ZERO
                    belopOpprUtbet = BigDecimal.valueOf(9000.00)
                    belopTilbakekreves = BigDecimal.valueOf(9000.00)
                    belopUinnkrevd = BigDecimal.ZERO
                    kodeKlasse = "PENAPGA"
                    skattProsent = BigDecimal.valueOf(31.123)
                }
            )
        )
        belopSkattMnd = BigDecimal(2802)
    }
)

fun createReturnertKravgrunnlagDto(): ReturnertKravgrunnlagDto {
    return ReturnertKravgrunnlagDto().apply {
        kravgrunnlagId = BigInteger.valueOf(152806)
        kodeStatusKrav = "NY"
        gjelderId = "10127435540" // mock verdi
        typeGjelderId = TypeGjelderDto.PERSON
        utbetalesTilId = "10127435540" // mock verdi
        typeUtbetId = TypeGjelderDto.PERSON
        kodeFagomraade = "PENAP"
        fagsystemId = "10000000000000000"
        datoVedtakFagsystem = LocalDate.of(2019, 3, 14).asXMLGregorianCalendar()
        belopSumFeilutbetalt = BigDecimal.valueOf(39000)
        enhetBosted = ENHET
        enhetAnsvarlig = ENHET
        datoKravDannet = LocalDate.of(2016, 6, 1).asXMLGregorianCalendar()
        periode = PeriodeDto().apply {
            fom = LocalDate.of(2016, 3, 1).asXMLGregorianCalendar()
            tom = LocalDate.of(2016, 5, 26).asXMLGregorianCalendar()
        }
    }
}
