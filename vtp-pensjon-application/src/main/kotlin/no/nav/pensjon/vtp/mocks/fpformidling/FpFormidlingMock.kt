package no.nav.pensjon.vtp.mocks.fpformidling

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.mocks.fpformidling.dto.BehandlingUuidDto
import no.nav.pensjon.vtp.mocks.fpformidling.dto.DokumentProdusertDto
import no.nav.pensjon.vtp.mocks.fpformidling.dto.DokumentbestillingDto
import no.nav.pensjon.vtp.mocks.fpformidling.dto.HentBrevmalerDto
import no.nav.pensjon.vtp.mocks.fpformidling.dto.TekstFraSaksbehandlerDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@RestController
@Tag(name = "/fpformidling")
@RequestMapping("/rest/fpformidling")
class FpFormidlingMock {
    private val dokumentProduksjon: MutableMap<UUID, MutableList<String>> = ConcurrentHashMap()
    private val saksbehandlerTekst: MutableMap<UUID, TekstFraSaksbehandlerDto?> = ConcurrentHashMap()

    @PostMapping(value = ["/hent-dokumentmaler"])
    @Operation(summary = "HentDokumentmalListe", description = "Returnerer tilgjengelige dokumentmaler")
    fun hentDokumentmalListe(request: BehandlingUuidDto?) = HentBrevmalerDto(emptyList())

    @PostMapping(value = ["brev/maler"])
    fun hentBrevmaler(uuidDto: BehandlingUuidDto?): HentBrevmalerDto = HentBrevmalerDto(emptyList())

    @PostMapping(value = ["brev/dokument-sendt"])
    fun erDokumentSendt(request: DokumentProdusertDto): Boolean {
        return dokumentProduksjon.getOrDefault(request.behandlingUuid, emptyList()).contains(request.dokumentMal)
    }

    @PostMapping(value = ["brev/bestill"])
    fun bestillDokument(request: DokumentbestillingDto) {
        dokumentProduksjon.computeIfAbsent(request.behandlingUuid) { CopyOnWriteArrayList() }
            .add(request.dokumentMal)
    }

    @PostMapping(value = ["saksbehandlertekst/hent"])
    fun hentSaksbehandlersTekst(uuidDto: BehandlingUuidDto): TekstFraSaksbehandlerDto? {
        return saksbehandlerTekst.getOrDefault(uuidDto.behandlingUuid, null)
    }

    @PostMapping(value = ["saksbehandlertekst/lagre"])
    fun lagreSaksbehandlersTekst(request: TekstFraSaksbehandlerDto) {
        saksbehandlerTekst[request.behandlingUuid] = request
    }
}
