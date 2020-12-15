package no.nav.pensjon.vtp.testmodell.inntektytelse

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold
import no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell
import no.nav.pensjon.vtp.testmodell.inntektytelse.arena.ArenaModell
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell
import no.nav.pensjon.vtp.testmodell.inntektytelse.sigrun.SigrunModell
import no.nav.pensjon.vtp.testmodell.inntektytelse.trex.TRexModell

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class InntektYtelseModell(
    @JsonProperty("arena")
    val arenaModell: ArenaModell? = null,

    @JsonProperty("trex")
    val tRexModell: TRexModell? = null,

    @JsonProperty("inntektskomponent")
    val inntektskomponentModell: InntektskomponentModell,

    @JsonProperty("aareg")
    val arbeidsforholdModell: ArbeidsforholdModell? = null,

    @JsonProperty("sigrun")
    val sigrunModell: SigrunModell? = null
) {
    fun finnArbeidsforhold(arbeidsforholdId: Long): Arbeidsforhold? {
        return arbeidsforholdModell
            ?.arbeidsforhold
            ?.firstOrNull { it.arbeidsforholdIdnav == arbeidsforholdId }
    }
}
