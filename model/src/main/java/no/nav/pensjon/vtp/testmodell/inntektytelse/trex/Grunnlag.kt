package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Grunnlag(
    @JsonProperty("status") private val status: Status,
    @JsonProperty("tema") private val tema: Tema,
    @JsonProperty("dekningsgrad") private val dekningsgrad: Prosent,
    @JsonProperty("fødselsdatoBarn") @JsonAlias("foedselsdatoBarn") private val fødselsdatoBarn: LocalDate,
    @JsonProperty("kategori") @JsonAlias("arbeidskategori") private val kategori: Arbeidskategori,
    @JsonProperty("arbeidsforhold") private val arbeidsforhold: List<Arbeidsforhold>,
    @JsonProperty("periode") private val periode: Periode,
    @JsonProperty("behandlingstema") private val behandlingsTema: Behandlingstema,
    @JsonProperty("identdato") private val identdato: LocalDate,
    @JsonProperty("iverksatt") private val iverksatt: LocalDate,
    @JsonProperty("opphørFom") @JsonAlias("opphoerFom") private val opphørFom: LocalDate,
    @JsonProperty("gradering") private val gradering: Int,
    @JsonProperty("opprinneligIdentdato") private val opprinneligIdentdato: LocalDate,
    @JsonProperty("registrert") private val registrert: LocalDate,
    @JsonProperty("saksbehandlerId") private val saksbehandlerId: String,
    @JsonProperty("vedtak") private val vedtak: List<Vedtak>
)
