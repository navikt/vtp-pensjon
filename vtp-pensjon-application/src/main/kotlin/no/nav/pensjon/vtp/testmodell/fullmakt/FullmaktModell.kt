package no.nav.pensjon.vtp.testmodell.fullmakt

import com.fasterxml.jackson.annotation.JsonInclude
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFullmakt
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
data class FullmaktModell(
    @Id
    val pid: String,
    val fullmakt: ASBOPenFullmakt
)
