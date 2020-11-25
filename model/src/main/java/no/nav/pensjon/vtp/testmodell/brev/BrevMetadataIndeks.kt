package no.nav.pensjon.vtp.testmodell.brev

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

class BrevMetadataIndeks {
    var brevMetadata : List<BrevMetadata> = emptyList()

    fun init(resource: String) {
        val mapper = JsonMapper.builder()
                .addModule(KotlinModule())
                .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                .build()
        brevMetadata = mapper.readValue(javaClass.getResource(resource).openStream())
    }

    fun getAll() : List<BrevMetadata> {
        return brevMetadata;
    }
}