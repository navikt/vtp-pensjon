package no.nav.pensjon.vtp.testmodell.organisasjon;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Navn {
    @JsonProperty("navnelinje")
    private List<String> navnelinje;

    public List<String> getNavnelinje() {
        return navnelinje;
    }

    public void setNavnelinje(List<String> navnelinje) {
        this.navnelinje = navnelinje;
    }
}
