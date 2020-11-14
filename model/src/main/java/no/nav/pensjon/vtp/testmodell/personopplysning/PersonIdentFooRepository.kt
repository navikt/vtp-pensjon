package no.nav.pensjon.vtp.testmodell.personopplysning

import org.springframework.data.repository.Repository
import java.util.stream.Stream

interface PersonIdentFooRepository : Repository<PersonIdentFoo, String> {
    fun findById(id: String): PersonIdentFoo?
    fun findAll(): Stream<PersonIdentFoo>
    fun save(personIdentFoo: PersonIdentFoo) : PersonIdentFoo
}
