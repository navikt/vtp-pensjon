package no.nav.pensjon.vtp.testmodell.personopplysning

import org.springframework.data.repository.Repository

interface PersonIdentFooRepository : Repository<PersonIdentFoo, String> {
    fun findById(id: String): PersonIdentFoo?
    fun findAll(): List<PersonIdentFoo>
    fun save(personIdentFoo: PersonIdentFoo): PersonIdentFoo
}
