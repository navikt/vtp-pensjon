package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgavebehandling.v2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFoo;
import no.nav.pensjon.vtp.mocks.oppgave.repository.Sporing;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.EndreOppgave;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgave;

public class Mapper {
    public static OppgaveFoo asOppaveFoo(Sporing saksbeh, OpprettOppgave oo) {
        OppgaveFoo o = new OppgaveFoo(saksbeh);

        o.setAktivFra(c(oo.getAktivFra()));
        o.setAktivTil(c(oo.getAktivTil()));
        o.setAnsvarligEnhetId(oo.getAnsvarligEnhetId());
        o.setAnsvarligId(oo.getAnsvarligId());

        o.setBeskrivelse(oo.getBeskrivelse());
        o.setBrukerId(oo.getBrukerId());
        o.setBrukertypeKode(oo.getBrukertypeKode());

        o.setDokumentId(oo.getDokumentId());

        o.setFagomradeKode(oo.getFagomradeKode());

        o.setHenvendelseId(oo.getHenvendelseId());

        o.setKravId(oo.getKravId());

        o.setLest(oo.getLest());

        o.setMappeId(oo.getMappeId());
        o.setMottattDato(c(oo.getMottattDato()));

        o.setNormDato(c(oo.getNormDato()));

        o.setOppfolging(oo.getOppfolging());
        o.setOppgavetypeKode(oo.getOppgavetypeKode());

        o.setPrioritetKode(oo.getPrioritetKode());

        o.setRevurderingstype(oo.getRevurderingstype());

        o.setSaksnummer(oo.getSaksnummer());
        o.setSkannetDato(c(oo.getSkannetDato()));
        o.setSoknadsId(oo.getSoknadsId());

        o.setUnderkategoriKode(oo.getUnderkategoriKode());

        return o;
    }

    public static OppgaveFoo asOppaveFoo(Sporing saksbeh, EndreOppgave oo) {
        OppgaveFoo o = new OppgaveFoo(saksbeh);

        o.setAktivFra(c(oo.getAktivFra()));
        o.setAktivTil(c(oo.getAktivTil()));
        o.setAnsvarligEnhetId(oo.getAnsvarligEnhetId());
        o.setAnsvarligId(oo.getAnsvarligId());

        o.setBeskrivelse(oo.getBeskrivelse());
        o.setBrukerId(oo.getBrukerId());
        o.setBrukertypeKode(oo.getBrukertypeKode());

        o.setDokumentId(oo.getDokumentId());

        o.setFagomradeKode(oo.getFagomradeKode());

        o.setHenvendelseId(oo.getHenvendelseId());

        o.setKravId(oo.getKravId());

        o.setLest(oo.getLest());

        o.setMappeId(oo.getMappeId());
        o.setMottattDato(c(oo.getMottattDato()));

        o.setNormDato(c(oo.getNormDato()));

        o.setOppfolging(oo.getOppfolging());
        o.setOppgavetypeKode(oo.getOppgavetypeKode());

        o.setPrioritetKode(oo.getPrioritetKode());

        o.setRevurderingstype(oo.getRevurderingstype());

        o.setSaksnummer(oo.getSaksnummer());
        o.setSkannetDato(c(oo.getSkannetDato()));
        o.setSoknadsId(oo.getSoknadsId());

        o.setUnderkategoriKode(oo.getUnderkategoriKode());

        return o;
    }

    private static LocalDate c(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
    }
}
