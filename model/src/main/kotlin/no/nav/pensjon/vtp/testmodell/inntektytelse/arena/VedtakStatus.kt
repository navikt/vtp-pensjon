package no.nav.pensjon.vtp.testmodell.inntektytelse.arena

@Suppress("unused", "SpellCheckingInspection")
enum class VedtakStatus(val termnavn: String) {
    IVERK("Iverksatt"),
    OPPRE("Opprettet"),
    INNST("Innstilt"),
    REGIS("Registrert"),
    MOTAT("Mottatt"),
    GODKJ("Godkjent"),
    AVSLU("Avsluttet"),
}
