package no.nav.pensjon.vtp.testmodell.kodeverk;

public enum Rolle {
    EKTE, BARN, FARA(true), MORA(true), SAMB, REPA, MMOR(true);

    private boolean forelder;

    Rolle() {
    }

    Rolle(boolean forelder) {
        this.forelder = forelder;
    }

    public boolean erForelder() {
        return forelder;
    }
}
