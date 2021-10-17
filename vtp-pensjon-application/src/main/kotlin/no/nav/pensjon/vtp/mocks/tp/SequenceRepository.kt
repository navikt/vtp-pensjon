package no.nav.pensjon.vtp.mocks.tp

import org.springframework.data.repository.Repository

interface SequenceRepository : Repository<Sequence, String> {
    fun save(entity: Sequence): Sequence

    fun findByName(id: String): Sequence?
}
