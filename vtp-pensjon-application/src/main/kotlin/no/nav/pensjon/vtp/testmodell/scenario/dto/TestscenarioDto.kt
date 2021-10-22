package no.nav.pensjon.vtp.testmodell.scenario.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.pensjon.vtp.testmodell.scenario.pensjon.PensjonTestScenario

data class TestscenarioDto(
    @JsonProperty("templateKey")
    val templateKey: String?,

    @JsonProperty("templateNavn")
    val templateNavn: String?,

    @JsonProperty("id")
    val testscenarioId: String,

    @JsonProperty("personopplysninger")
    val personopplysninger: TestscenarioPersonopplysningDto,

    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @JsonProperty("variabler")
    val variabler: Map<String, String?>,

    @JsonProperty("pensjonTestScenario")
    val pensjonTestScenario: PensjonTestScenario,

    @JsonProperty("scenariodata")
    val scenariodataDto: TestscenariodataDto,

    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @JsonProperty("scenariodataAnnenpart")
    val scenariodataAnnenpartDto: TestscenariodataDto?
)
