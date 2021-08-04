package no.nav.pensjon.vtp.testmodell.dkif

import org.springframework.data.repository.Repository

interface DkifRepository : Repository<Kontaktinfo, String> {
    fun findById(ident: String): Kontaktinfo?
    fun saveAll(modeller: Iterable<Kontaktinfo>)
}
