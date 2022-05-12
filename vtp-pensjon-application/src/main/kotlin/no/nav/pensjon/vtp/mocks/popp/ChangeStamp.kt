package no.nav.pensjon.vtp.mocks.popp

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*

data class ChangeStamp @JsonCreator constructor(
    @JsonProperty("createdDate") val createdDate: Date,
    @JsonProperty("createdBy") val createdBy: String,
    @JsonProperty("updatedDate") val updatedDate: Date,
    @JsonProperty("updatedBy") var updatedBy: String
) : Serializable {
    companion object {
        private const val serialVersionUID = 61541164562562288L
    }
}
