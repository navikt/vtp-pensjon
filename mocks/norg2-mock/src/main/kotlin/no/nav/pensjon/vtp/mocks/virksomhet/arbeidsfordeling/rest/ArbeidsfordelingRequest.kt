package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT, content = JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    creatorVisibility = JsonAutoDetect.Visibility.NONE
)
data class ArbeidsfordelingRequest(
    @JsonProperty(value = "behandlingstema")
    private val behandlingstema: String?,
    @JsonProperty(value = "behandlingstype")
    private val behandlingstype: String?,
    @JsonProperty(value = "diskresjonskode")
    val diskresjonskode: String?,
    @JsonProperty(value = "geografiskOmraade")
    private val geografiskOmraade: String?,
    @JsonProperty(value = "oppgavetype")
    private val oppgavetype: String?,
    @JsonProperty(value = "tema")
    val tema: String?,
    @JsonProperty(value = "temagruppe")
    private val temagruppe: String?
)
