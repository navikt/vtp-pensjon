package no.nav.pensjon.vtp.testmodell.personopplysning

import org.springframework.stereotype.Component
import java.util.*
import java.util.Optional.empty
import java.util.Optional.ofNullable

@Component
class PersonIndeks(private val personIdentFooRepository: PersonIdentFooRepository) {
    @Synchronized
    fun indekserPersonopplysningerByIdent(pers: Personopplysninger) {
        personIdentFooRepository.save(PersonIdentFoo(pers.søker.ident, pers))
        pers.annenPart?.ident?.let { personIdentFooRepository.save(PersonIdentFoo(it, pers)) }
    }

    fun findById(id: String): Optional<Personopplysninger> {
        return personIdentFooRepository.findById(id)
                ?.personopplysninger
                ?.let { ofNullable(it) }
                ?: empty()
    }
}
