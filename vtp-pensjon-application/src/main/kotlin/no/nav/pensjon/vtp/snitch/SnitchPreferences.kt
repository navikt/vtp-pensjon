package no.nav.pensjon.vtp.snitch

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class SnitchPreferences(
    @Id
    @JsonIgnore
    val id: String = "default",
    val ignoredPaths: Set<String> = emptySet()
) {
    fun isIgnored(requestResponse: RequestResponse): Boolean {
        return ignoredPaths.contains(requestResponse.path)
    }
}
