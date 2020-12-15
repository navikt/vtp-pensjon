package no.nav.pensjon.vtp.testmodell.inntektytelse.trex;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Arbeidsforhold {

    private final Orgnummer orgnr;
    private final Integer inntekt;
    private final Inntektsperiode inntektperiode;
    private final Boolean refusjon;

    @JsonCreator
    public Arbeidsforhold(
            @JsonProperty("orgnr") @JsonAlias("arbeidsgiverOrgnr") Orgnummer orgnr,
            @JsonProperty("inntekt") @JsonAlias("inntektForPerioden") Integer inntekt,
            @JsonProperty("inntektsperiode") Inntektsperiode inntektperiode,
            @JsonProperty("refusjon") Boolean refusjon) {
        this.orgnr = orgnr;
        this.inntekt = inntekt;
        this.inntektperiode = inntektperiode;
        this.refusjon = refusjon;
    }

    public Orgnummer getOrgnr() {
        return orgnr;
    }

    public Integer getInntekt() {
        return inntekt;
    }

    public Inntektsperiode getInntektperiode() {
        return inntektperiode;
    }

    public Boolean getRefusjon() {
        return refusjon;
    }
}
