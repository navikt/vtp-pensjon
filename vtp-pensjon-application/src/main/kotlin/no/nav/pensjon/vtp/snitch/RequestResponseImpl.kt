package no.nav.pensjon.vtp.snitch

import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct

@Repository
class RequestResponseImpl(
    val mongoOperations: MongoOperations
) : RequestResponseCustom {
    @PostConstruct
    fun setupCappedCollection() {
        if (!mongoOperations.collectionExists(RequestResponse::class.java)) {
            val collectionOptions = CollectionOptions.empty()
                .size(10_000_000)
                .capped()
                .maxDocuments(100)
            mongoOperations.createCollection<RequestResponse>(RequestResponse::class.java, collectionOptions)
        }
    }
}
