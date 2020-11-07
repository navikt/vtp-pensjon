package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgave.v3

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFoo
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.*


fun enhet(enhetId: String, enhetNavn: String): Enhet {
    val enhet = Enhet()
    enhet.id = enhetId
    enhet.navn = enhetNavn
    return enhet
}

fun no.nav.pensjon.vtp.mocks.oppgave.repository.Sporing.asSporingsdetalj(): Sporingsdetalj {
    val sporingsdetalj = Sporingsdetalj()
    sporingsdetalj.enhet = enhet(enhetId, enhetNavn)
    sporingsdetalj.ident = this.ansattIdent
    return sporingsdetalj
}

fun String.asFagomrade(): Fagomrade {
    val fagomrade = Fagomrade()
    fagomrade.kode = this
    return fagomrade
}

fun OppgaveFoo.asSporing() : Sporing {
    val sporing = Sporing()
    sporing.opprettetInfo = this.opprettetSporing?.asSporingsdetalj()
    sporing.endretInfo = this.endretSporing?.asSporingsdetalj()
    return sporing
}

fun String.asUnderkategori(): Underkategori {
    val underkategori = Underkategori()
    underkategori.kode = this
    return underkategori
}

fun String.asPrioritet(): Prioritet {
    val prioritet = Prioritet()
    prioritet.kode = this
    return prioritet
}

fun String.asOppgavetype(): Oppgavetype {
    val oppgavetype = Oppgavetype()
    oppgavetype.kode = this
    return oppgavetype
}

fun String.asMappe(): Mappe {
    val mappe = Mappe()
    mappe.mappeId = this
    return mappe
}

fun String.asBruker(): Bruker {
    val bruker = Bruker()
    bruker.brukerId = this
    return bruker
}

fun OppgaveFoo.asOppgave3(): Oppgave {
    val o = Oppgave()
    o.aktivFra = this.aktivFra?.asXMLGregorianCalendar()
    o.aktivTil = this.aktivTil?.asXMLGregorianCalendar()
    o.ansvarligEnhetId = this.ansvarligEnhetId
    //o.setAnsvarligEnhetNavn(this.ansv);
    o.ansvarligId = this.ansvarligId
    //o.setAnsvarligNavn();
    o.beskrivelse = this.beskrivelse
    o.dokumentId = this.dokumentId
    o.fagomrade = this.fagomradeKode?.asFagomrade()
    o.gjelder = this.brukerId?.asBruker()
    o.henvendelseId = this.henvendelseId
    //o.setHenvendelsetype(this.getH);
    o.kravId = this.kravId
    o.isLest = this.isLest
    o.mappe = this.mappeId?.asMappe()
    o.mottattDato = this.mottattDato?.asXMLGregorianCalendar()
    o.normDato = this.normDato?.asXMLGregorianCalendar()
    o.oppfolging = this.oppfolging
    o.oppgaveId = this.oppgaveId
    o.oppgavetype = this.oppgavetypeKode?.asOppgavetype()
    o.prioritet = this.prioritetKode?.asPrioritet()
    o.revurderingstype = this.revurderingstype
    o.saksnummer = this.saksnummer
    o.skannetDato = this.skannetDato?.asXMLGregorianCalendar()
    o.soknadsId = this.soknadsId
    o.sporing = this.asSporing()
    //o.setStatus();
    o.versjon = this.version
    o.underkategori = this.underkategoriKode?.asUnderkategori()
    //o.setUtvidelse(this.ut);
    return o
}
