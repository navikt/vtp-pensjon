package no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold

object ArbeidsforholdIdNav {
    private var arbeidsforholdIdNav = 10000L

    operator fun next(): Long {
        return ++arbeidsforholdIdNav
    }
}
