package no.nav.pensjon.vtp.mocks.oppgave.repository;

import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;

public class Sporing {
    private final String ansattIdent;
    private final Norg2Modell enhet;

    public Sporing(String ansattIdent, Norg2Modell enhet) {
        this.ansattIdent = ansattIdent;
        this.enhet = enhet;
    }

    public String getEnhetId() {
        return enhet.getEnhetId();
    }

    public String getEnhetNavn() {
        return enhet.getNavn();
    }

    public String getAnsattIdent() {
        return ansattIdent;
    }
}