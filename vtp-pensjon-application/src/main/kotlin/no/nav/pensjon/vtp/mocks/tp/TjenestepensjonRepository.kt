package no.nav.pensjon.vtp.mocks.tp

import org.springframework.data.mongodb.repository.MongoRepository

interface TjenestepensjonRepository : MongoRepository<Tjenestepensjon, String> {
    fun findByForholdForholdId(forholdId: String): Tjenestepensjon?
    fun findByPid(pid: String): Tjenestepensjon?
}
