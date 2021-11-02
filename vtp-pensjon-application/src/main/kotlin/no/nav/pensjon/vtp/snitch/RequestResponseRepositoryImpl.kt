package no.nav.pensjon.vtp.snitch

import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct

@Repository
class RequestResponseRepositoryImpl(
    val mongoOperations: MongoOperations,
    val mongoTemplate: MongoTemplate,
) : RequestResponseRespositoryCustom {
    @PostConstruct
    fun setupCappedCollection() {
        if (!mongoOperations.collectionExists(RequestResponse::class.java)) {
            val collectionOptions = CollectionOptions.empty()
                .size(10_000_000)
                .capped()
                .maxDocuments(100)
            mongoOperations.createCollection(RequestResponse::class.java, collectionOptions)
        }
    }

    override fun deleteAll() {
        mongoTemplate.dropCollection<RequestResponse>()
        setupCappedCollection()
    }
}
