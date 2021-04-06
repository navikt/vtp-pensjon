package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgave.v3

import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFoo
import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.*

fun no.nav.pensjon.vtp.mocks.oppgave.repository.Sporing.asSporingsdetalj(): Sporingsdetalj {
    return Sporingsdetalj().apply {
        enhet = Enhet().apply {
            id = enhetId.toString()
            navn = enhetNavn
        }
        ident = ansattIdent
    }
}

fun OppgaveFoo.asOppgave3(): Oppgave {
    return Oppgave().apply {
        aktivFra = this@asOppgave3.aktivFra?.asXMLGregorianCalendar()
        aktivTil = this@asOppgave3.aktivTil?.asXMLGregorianCalendar()
        ansvarligEnhetId = this@asOppgave3.ansvarligEnhetId
        ansvarligEnhetNavn = this@asOppgave3.ansvarligEnhetId
        ansvarligId = this@asOppgave3.ansvarligId
        ansvarligNavn = this@asOppgave3.ansvarligId
        beskrivelse = this@asOppgave3.beskrivelse
        dokumentId = this@asOppgave3.dokumentId
        fagomrade = this@asOppgave3.fagomradeKode?.let {
            Fagomrade().apply {
                kode = it
            }
        }
        gjelder = this@asOppgave3.brukerId?.let {
            Bruker().apply {
                brukerId = it
            }
        }
        henvendelseId = this@asOppgave3.henvendelseId
        // henvendelsetype = XXX
        kravId = this@asOppgave3.kravId
        isLest = this@asOppgave3.isLest
        mappe = this@asOppgave3.mappeId?.let {
            Mappe().apply {
                this.mappeId = it
            }
        }
        mottattDato = this@asOppgave3.mottattDato?.asXMLGregorianCalendar()
        normDato = this@asOppgave3.normDato?.asXMLGregorianCalendar()
        oppfolging = this@asOppgave3.oppfolging
        oppgaveId = this@asOppgave3.oppgaveId
        oppgavetype = this@asOppgave3.oppgavetypeKode?.let {
            Oppgavetype().apply {
                kode = it
            }
        }
        prioritet = this@asOppgave3.prioritetKode?.let {
            Prioritet().apply {
                kode = it
            }
        }
        revurderingstype = this@asOppgave3.revurderingstype
        saksnummer = this@asOppgave3.saksnummer
        skannetDato = this@asOppgave3.skannetDato?.asXMLGregorianCalendar()
        soknadsId = this@asOppgave3.soknadsId
        sporing = Sporing().apply {
            opprettetInfo = this@asOppgave3.opprettetSporing?.asSporingsdetalj()
            endretInfo = this@asOppgave3.endretSporing?.asSporingsdetalj()
        }
        // setStatus();
        versjon = this@asOppgave3.version
        underkategori = this@asOppgave3.underkategoriKode?.let {
            Underkategori().apply {
                kode = it
            }
        }
        // utvidelse = XXX
    }
}
