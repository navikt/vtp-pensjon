package no.nav.pensjon.vtp.mocks.oppgave.repository;

import static java.util.UUID.randomUUID;

import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgave;

public class OppgaveFoo {
    private final String id;
    private final int version;
    private final Sporing opprettetSporing;
    private final Sporing endretSporing;
    private final OpprettOppgave opprettOppgave;

    public OppgaveFoo(String id, int version, Sporing opprettetSporing, Sporing endretSporing, OpprettOppgave opprettOppgave) {
        this.id = id;
        this.version = version;
        this.opprettetSporing = opprettetSporing;
        this.opprettOppgave = opprettOppgave;
        this.endretSporing = endretSporing;
    }

    public OppgaveFoo(Sporing opprettetSporing, OpprettOppgave opprettOppgave) {
        this.opprettetSporing = opprettetSporing;
        this.endretSporing = opprettetSporing;
        this.id = randomUUID().toString();
        this.version = 1;
        this. opprettOppgave = opprettOppgave;
    }

    public String getOppgaveId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public OpprettOppgave getOpprettOppgave() {
        return opprettOppgave;
    }

    public OppgaveFoo withIncrementedVersion() {
        return new OppgaveFoo(id, version + 1, opprettetSporing, endretSporing, opprettOppgave);
    }

    public String getOpprettetEnhetId() {
        return opprettetSporing.getEnhetId();
    }

    public String getOpprettetEnhetNavn() {
        return opprettetSporing.getEnhetNavn();
    }

    public String getOpprettetAnsattIdent() {
        return opprettetSporing.getAnsattIdent();
    }

    public String getEndretAnsattIdent() {
        return endretSporing.getAnsattIdent();
    }

    public String getEndretEnhetId() {
        return endretSporing.getEnhetId();
    }

    public String getEndretEnhetNavn() {
        return endretSporing.getEnhetNavn();
    }
}
