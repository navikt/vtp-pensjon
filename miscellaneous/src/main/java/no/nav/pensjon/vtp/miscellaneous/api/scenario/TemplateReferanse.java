package no.nav.pensjon.vtp.miscellaneous.api.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;

/** Beskriver en template,inklusiv liste av variable og deres verdier. */
public class TemplateReferanse {

    @JsonProperty("key")
    private String key;

    @JsonProperty("navn")
    private String navn;

    public TemplateReferanse(String key, String navn) {
        this.key = key;
        this.navn = navn;
    }

    public static TemplateReferanse fromTestscenarioTemplate(TestscenarioTemplate testscenarioTemplate) {
        return new TemplateReferanse(testscenarioTemplate.getTemplateKey(), testscenarioTemplate.getTemplateNavn());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

}
