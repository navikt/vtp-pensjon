package no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2.PdfGenerering.PdfGeneratorUtil
import no.nav.pensjon.vtp.testmodell.dokument.JournalpostModellGenerator.lagJournalpostUstrukturertDokument
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.DokumentproduksjonV2
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.informasjon.Person
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.*
import javax.jws.HandlerChain
import javax.jws.WebService
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/dokprod/ws/dokumentproduksjon/v2"])
@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.DokumentproduksjonV2")
@HandlerChain(file = "/Handler-chain.xml")
class DokumentproduksjonV2MockImpl(private val journalRepository: JournalRepository) : DokumentproduksjonV2 {
    override fun ping() {
    }

    override fun produserDokumentutkast(request: ProduserDokumentutkastRequest): ProduserDokumentutkastResponse {
        return ProduserDokumentutkastResponse().apply {
            dokumentutkast = PdfGeneratorUtil().genererPdfByteArrayFraString(request.any.toString())
        }
    }

    override fun avbrytVedlegg(avbrytVedleggRequest: AvbrytVedleggRequest) {
        throw NotImplementedException()
    }

    override fun produserIkkeredigerbartDokument(request: ProduserIkkeredigerbartDokumentRequest) =
        ProduserIkkeredigerbartDokumentResponse().apply {
            val bruker = request.dokumentbestillingsinformasjon.bruker as Person

            val journalpost = journalRepository.save(
                lagJournalpostUstrukturertDokument(
                    bruker.ident, DokumenttypeId.fromCode(request.dokumentbestillingsinformasjon.dokumenttypeId)
                )
            )

            dokumentId = journalpost.dokumentModellList[0].dokumentId
            journalpostId = journalpost.journalpostId
        }

    override fun knyttVedleggTilForsendelse(knyttVedleggTilForsendelseRequest: KnyttVedleggTilForsendelseRequest): Unit =
        throw NotImplementedException()

    override fun produserRedigerbartDokument(produserRedigerbartDokumentRequest: ProduserRedigerbartDokumentRequest): ProduserRedigerbartDokumentResponse =
        throw NotImplementedException()

    override fun avbrytForsendelse(avbrytForsendelseRequest: AvbrytForsendelseRequest): Unit =
        throw NotImplementedException()

    override fun ferdigstillForsendelse(request: FerdigstillForsendelseRequest) = Unit

    override fun redigerDokument(redigerDokumentRequest: RedigerDokumentRequest) =
        throw NotImplementedException()

    override fun produserIkkeredigerbartVedlegg(produserIkkeredigerbartVedleggRequest: ProduserIkkeredigerbartVedleggRequest) =
        throw NotImplementedException()

    override fun endreDokumentTilRedigerbart(endreDokumentTilRedigerbartRequest: EndreDokumentTilRedigerbartRequest): Unit =
        throw NotImplementedException()
}
