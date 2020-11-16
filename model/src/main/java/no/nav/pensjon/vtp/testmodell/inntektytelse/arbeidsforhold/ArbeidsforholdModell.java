package no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArbeidsforholdModell {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("arbeidsforhold")
    private List<Arbeidsforhold> arbeidsforhold = new ArrayList<>();

    public List<Arbeidsforhold> getArbeidsforhold() {
        return arbeidsforhold;
    }

    public Stream<Arbeidsforhold> stream() {
        return arbeidsforhold.stream();
    }

    public void setArbeidsforhold(List<Arbeidsforhold> arbeidsforhold) {
        this.arbeidsforhold.clear();
        this.arbeidsforhold.addAll(arbeidsforhold);
    }

}
