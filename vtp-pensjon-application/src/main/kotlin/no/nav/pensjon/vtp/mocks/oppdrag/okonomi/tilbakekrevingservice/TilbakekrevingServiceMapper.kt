package no.nav.pensjon.vtp.mocks.oppdrag.okonomi.tilbakekrevingservice

import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagAnnulerRequest
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagAnnulerResponse
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentDetaljResponse
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentListeResponse
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakRequest
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakResponse
import no.nav.tilbakekreving.typer.v1.MmelDto

private const val KVITTERING_OK_KODE = "00"

fun opprettTilbakekrevingVedtakResponse(request: TilbakekrevingsvedtakRequest): TilbakekrevingsvedtakResponse {
    return TilbakekrevingsvedtakResponse().apply {
        mmel = MmelDto().apply {
            alvorlighetsgrad = KVITTERING_OK_KODE
            systemId = "1"
        }
        tilbakekrevingsvedtak = request.tilbakekrevingsvedtak
    }
}

fun opprettKravgrunnlagHentDetaljResponse(): KravgrunnlagHentDetaljResponse {
    return KravgrunnlagHentDetaljResponse().apply {
        mmel = MmelDto().apply {
            alvorlighetsgrad = KVITTERING_OK_KODE
            systemId = "1"
        }
        detaljertkravgrunnlag = hentGrunnlag()
    }
}

fun opprettKravgrunnlagHentListeResponse(): KravgrunnlagHentListeResponse {
    return KravgrunnlagHentListeResponse().apply {
        mmel = MmelDto().apply {
            alvorlighetsgrad = KVITTERING_OK_KODE
            systemId = "1"
        }
        kravgrunnlagListe.add(createReturnertKravgrunnlagDto())
    }
}

fun opprettKravgrunnlagAnnulerResponse(kravgrunnlagAnnulerRequest: KravgrunnlagAnnulerRequest): KravgrunnlagAnnulerResponse {
    return KravgrunnlagAnnulerResponse().apply {
        mmel = MmelDto().apply {
            alvorlighetsgrad = KVITTERING_OK_KODE
            systemId = "1"
        }
        annullerkravgrunnlag = kravgrunnlagAnnulerRequest.annullerkravgrunnlag
    }
}
