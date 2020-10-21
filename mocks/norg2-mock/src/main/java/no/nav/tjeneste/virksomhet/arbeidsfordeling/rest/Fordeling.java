package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

public class Fordeling {
    private String tema;
    private String enhetNummer;

    public Fordeling() {
    }

    public Fordeling(String tema) {
        this.tema = tema;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getEnhetNummer() {
        return enhetNummer;
    }

    public void setEnhetNummer(String enhetNummer) {
        this.enhetNummer = enhetNummer;
    }

    @Override
    public String toString() {
        return "Fordeling{" +
                "tema='" + tema + '\'' +
                ", enhetNummer='" + enhetNummer + '\'' +
                '}';
    }
}
