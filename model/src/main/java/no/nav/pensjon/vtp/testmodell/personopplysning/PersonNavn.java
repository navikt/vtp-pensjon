package no.nav.pensjon.vtp.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonNavn {
    @JsonProperty("fornavn")
    private String fornavn;

    @JsonProperty("etternavn")
    private String etternavn;

    @JsonProperty("kjønn")
    private BrukerModell.Kjønn kjønn;

    
    public PersonNavn() {
        
    }

    public PersonNavn(String fornavn, String etternavn, BrukerModell.Kjønn kjønn) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
    }


    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public BrukerModell.Kjønn getKjønn() {
        return kjønn;
    }

    public void setKjønn(BrukerModell.Kjønn kjønn) {
        this.kjønn = kjønn;
    }

    public String getFulltnavn() {
        return getFornavn() + " " + getEtternavn();
    }
}
