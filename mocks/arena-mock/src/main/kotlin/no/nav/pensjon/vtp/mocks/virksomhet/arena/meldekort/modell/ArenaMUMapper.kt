package no.nav.pensjon.vtp.mocks.virksomhet.arena.meldekort.modell

import no.nav.pensjon.vtp.core.util.asLocalDate
import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.inntektytelse.arena.*
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.ObjectFactory
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.Vedtaksstatuser
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeRequest
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeResponse
import java.time.LocalDate
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.ObjectFactory as MeldingerObjectFactory

class ArenaMUMapper {
    private val objectFactory = ObjectFactory()
    private val meldingerObjectFactory = MeldingerObjectFactory()

    fun mapArenaSaker(
        request: FinnMeldekortUtbetalingsgrunnlagListeRequest,
        arenaSaker: List<ArenaSak>
    ): FinnMeldekortUtbetalingsgrunnlagListeResponse {
        val fom = request.periode?.fom?.asLocalDate()
        val tom = request.periode?.tom?.asLocalDate()

        val temaer = request.temaListe
            .map { it.value }

        return meldingerObjectFactory.createFinnMeldekortUtbetalingsgrunnlagListeResponse().apply {
            meldekortUtbetalingsgrunnlagListe += arenaSaker
                .filterNot { filtrerVekkSak(it, temaer) }
                .map { arenaSak ->
                    objectFactory.createSak().apply {
                        fagsystemSakId = arenaSak.saksnummer
                        tema = objectFactory.createTema().apply {
                            value = arenaSak.tema.name
                        }
                        saksstatus = mapSakStatus(arenaSak.status)
                        vedtakListe.addAll(
                            mapArenaVedtakListe(
                                arenaSak.vedtak
                                    .filter { filtrerVedtak(it, fom, tom) }
                            )
                        )
                    }
                }
        }
    }

    private fun filtrerVedtak(vedtak: ArenaVedtak, fom: LocalDate?, tom: LocalDate?): Boolean {
        return if (fom == null && tom == null) {
            true
        } else {
            (
                (fom == null || fom.isBefore(vedtak.fom) || fom.isEqual(vedtak.fom)) &&
                    (tom == null || tom.isAfter(vedtak.tom) || tom.isEqual(vedtak.tom))
                )
        }
    }

    private fun filtrerVekkSak(arenaSak: ArenaSak, temaer: List<String>): Boolean {
        return temaer.isNotEmpty() && !temaer.contains(arenaSak.tema.name)
    }

    private fun mapSakStatus(status: SakStatus) = objectFactory.createSaksstatuser().apply {
        termnavn = status.termnavn
        value = status.name
    }

    private fun mapArenaVedtakListe(arenaVedtakList: List<ArenaVedtak>?) = arenaVedtakList
        ?.map {
            objectFactory.createVedtak().apply {
                vedtaksstatus = mapVedtakStatus(it.status)
                vedtaksperiode = objectFactory.createPeriode().apply {
                    fom = it.fom?.asXMLGregorianCalendar()
                    tom = it.tom?.asXMLGregorianCalendar()
                }
                datoKravMottatt = it.kravMottattDato?.asXMLGregorianCalendar()
                vedtaksdato = it.vedtakDato?.asXMLGregorianCalendar()
                meldekortListe.addAll(mapArenaMeldekortListe(it.meldekort))
                dagsats = it.dagsats.toDouble()
            }
        }
        ?: emptyList()

    private fun mapVedtakStatus(status: VedtakStatus): Vedtaksstatuser {
        return objectFactory.createVedtaksstatuser().apply {
            termnavn = status.termnavn
            value = status.name
        }
    }

    private fun mapArenaMeldekortListe(meldekortList: List<ArenaMeldekort>?) = meldekortList
        ?.map {
            objectFactory.createMeldekort().apply {
                dagsats = it.dagsats.toDouble()
                beloep = it.beløp.toDouble()
                utbetalingsgrad = it.utbetalingsgrad.toDouble()
                meldekortperiode = objectFactory.createPeriode().apply {
                    fom = it.fom?.asXMLGregorianCalendar()
                    tom = it.tom?.asXMLGregorianCalendar()
                }
            }
        }
        ?: emptyList()
}
