package no.nav.pensjon.vtp.testmodell.personopplysning

import org.springframework.data.repository.Repository

interface PersonModellRepository : Repository<PersonModell, String> {
    fun findById(ident: String): PersonModell?
    fun findByAktørIdent(ident: String): PersonModell?
    fun findAll(): List<PersonModell>
    fun save(bruker: PersonModell): PersonModell?
}
