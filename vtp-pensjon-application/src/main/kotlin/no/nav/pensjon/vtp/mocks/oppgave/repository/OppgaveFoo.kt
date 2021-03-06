package no.nav.pensjon.vtp.mocks.oppgave.repository

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*

@Document
data class OppgaveFoo(
    @Id
    val oppgaveId: String = UUID.randomUUID().toString(),
    @Version
    val version: Int = 0,
    val opprettetSporing: Sporing?,
    val endretSporing: Sporing?,
    val brukerId: String?,
    val brukertypeKode: String?,
    val oppgavetypeKode: String?,
    val fagomradeKode: String?,
    val underkategoriKode: String?,
    val prioritetKode: String?,
    val beskrivelse: String?,
    val oppfolging: String?,
    val aktivFra: LocalDate?,
    val aktivTil: LocalDate?,
    val ansvarligEnhetId: String?,
    val ansvarligId: String?,
    val dokumentId: String?,
    val mottattDato: LocalDate?,
    val normDato: LocalDate?,
    val saksnummer: String?,
    val skannetDato: LocalDate?,
    val soknadsId: String?,
    val henvendelseId: String?,
    val kravId: String?,
    val isLest: Boolean,
    val mappeId: String?,
    val revurderingstype: String?
)
