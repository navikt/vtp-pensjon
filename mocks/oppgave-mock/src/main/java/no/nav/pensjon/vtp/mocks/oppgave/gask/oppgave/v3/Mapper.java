package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgave.v3;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFoo;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Bruker;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Enhet;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Fagomrade;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Mappe;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Oppgave;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Oppgavetype;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Prioritet;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Sporingsdetalj;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Underkategori;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgave;

public class Mapper {
    public static Oppgave asOppgave3(OppgaveFoo oppgaveFoo) {
        OpprettOppgave oo = oppgaveFoo.getOpprettOppgave();

        Oppgave o = new Oppgave();

        o.setAktivFra(asXMLGregorianCalendar(oo.getAktivFra()));
        o.setAktivTil(asXMLGregorianCalendar(oo.getAktivTil()));
        o.setAnsvarligEnhetId(oo.getAnsvarligEnhetId());
        //o.setAnsvarligEnhetNavn(oo.ansv);
        o.setAnsvarligId(oo.getAnsvarligId());
        //o.setAnsvarligNavn();

        o.setBeskrivelse(oo.getBeskrivelse());

        o.setDokumentId(oo.getDokumentId());

        o.setFagomrade(asFagomrade(oo.getFagomradeKode()));

        o.setGjelder(asBruker(oo.getBrukerId()));

        o.setHenvendelseId(oo.getHenvendelseId());
        //o.setHenvendelsetype(oo.getH);

        o.setKravId(oo.getKravId());

        o.setLest(oo.getLest());

        o.setMappe(asMappe(oo.getMappeId()));
        o.setMottattDato(asXMLGregorianCalendar(oo.getMottattDato()));

        o.setNormDato(asXMLGregorianCalendar(oo.getNormDato()));

        o.setOppfolging(oo.getOppfolging());
        o.setOppgaveId(oppgaveFoo.getOppgaveId());
        o.setOppgavetype(asOppgavetype(oo.getOppgavetypeKode()));

        o.setPrioritet(asPrioritet(oo.getPrioritetKode()));

        o.setRevurderingstype(oo.getRevurderingstype());

        o.setSaksnummer(oo.getSaksnummer());
        o.setSkannetDato(asXMLGregorianCalendar(oo.getSkannetDato()));
        o.setSoknadsId(oo.getSoknadsId());

        o.setSporing(asSporing(oppgaveFoo));
        //o.setStatus();

        o.setVersjon(oppgaveFoo.getVersion());

        o.setUnderkategori(asUnderkategori(oo.getUnderkategoriKode()));
        //o.setUtvidelse(oo.ut);

        return o;
    }

    private static no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Sporing asSporing(OppgaveFoo oppgaveFoo) {

        final no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Sporing sporing = new no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Sporing();
        sporing.setOpprettetInfo(createSporingsdetalj(oppgaveFoo.getOpprettetAnsattIdent(), oppgaveFoo.getOpprettetEnhetId(), oppgaveFoo.getOpprettetEnhetNavn()));
        sporing.setEndretInfo(createSporingsdetalj(oppgaveFoo.getEndretAnsattIdent(), oppgaveFoo.getEndretEnhetId(), oppgaveFoo.getEndretEnhetNavn()));

        return sporing;
    }

    private static Sporingsdetalj createSporingsdetalj(String ansattIdent, String enhetId, String enhetNavn) {
        final Enhet enhet = new Enhet();
        enhet.setId(enhetId);
        enhet.setNavn(enhetNavn);

        final Sporingsdetalj sporingsdetalj = new Sporingsdetalj();
        sporingsdetalj.setEnhet(enhet);
        sporingsdetalj.setIdent(ansattIdent);
        return sporingsdetalj;
    }

    private static Underkategori asUnderkategori(String underkategoriKode) {
        final Underkategori underkategori = new Underkategori();
        underkategori.setKode(underkategoriKode);
        return underkategori;
    }

    private static Prioritet asPrioritet(String prioritetKode) {
        final Prioritet prioritet = new Prioritet();
        prioritet.setKode(prioritetKode);
        return prioritet;
    }

    private static Oppgavetype asOppgavetype(String oppgavetypeKode) {
        final Oppgavetype oppgavetype = new Oppgavetype();
        oppgavetype.setKode(oppgavetypeKode);
        return oppgavetype;
    }

    private static Mappe asMappe(String mappeId) {
        final Mappe mappe = new Mappe();
        mappe.setMappeId(mappeId);
        return mappe;
    }

    private static Bruker asBruker(String brukerId) {
        final Bruker bruker = new Bruker();
        bruker.setBrukerId(brukerId);
        return bruker;
    }

    private static Fagomrade asFagomrade(String fagomradeKode) {
        final Fagomrade fagomrade = new Fagomrade();
        fagomrade.setKode(fagomradeKode);
        return fagomrade;
    }

    private static XMLGregorianCalendar asXMLGregorianCalendar(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(calendar.getTime());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
