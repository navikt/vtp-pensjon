package no.nav.pensjon.vtp.testmodell.personopplysning

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

@Repository
class PersonIdentFooRepositoryInMemory : PersonIdentFooRepository {
    private val idToPersonIdentFoo: MutableMap<String, PersonIdentFoo> = ConcurrentHashMap()

    override fun findById(id: String): PersonIdentFoo? {
        return idToPersonIdentFoo[id]
    }

    override fun findAll(): Stream<PersonIdentFoo> {
        return idToPersonIdentFoo.values.stream()
    }

    override fun save(personIdentFoo: PersonIdentFoo): PersonIdentFoo {
        idToPersonIdentFoo[personIdentFoo.ident] = personIdentFoo
        return personIdentFoo
    }
}