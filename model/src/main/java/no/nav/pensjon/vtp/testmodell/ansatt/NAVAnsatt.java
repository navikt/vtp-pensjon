package no.nav.pensjon.vtp.testmodell.ansatt;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NAVAnsatt {
    @JsonProperty
    public String cn;
    @JsonProperty
    public String givenname;
    @JsonProperty
    public String sn;
    @JsonProperty
    public String displayName;
    @JsonProperty
    public String email;
    @JsonProperty
    public List<String> groups;
    @JsonProperty
    public List<String> enheter;

    public String getDisplayName() {
        return displayName;
    }
}
