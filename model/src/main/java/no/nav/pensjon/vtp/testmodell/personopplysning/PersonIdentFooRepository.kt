package no.nav.pensjon.vtp.testmodell.personopplysning

import java.util.stream.Stream

interface PersonIdentFooRepository {
    fun findById(id: String): PersonIdentFoo?
    fun findAll(): Stream<PersonIdentFoo>
    fun save(personIdentFoo: PersonIdentFoo) : PersonIdentFoo
}
