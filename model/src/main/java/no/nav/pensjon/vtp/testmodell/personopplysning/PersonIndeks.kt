package no.nav.pensjon.vtp.testmodell.personopplysning

import org.springframework.stereotype.Component

@Component
class PersonIndeks(private val personIdentFooRepository: PersonIdentFooRepository) {
    @Synchronized
    fun indekserPersonopplysningerByIdent(pers: Personopplysninger) {
        personIdentFooRepository.save(PersonIdentFoo(pers.søker.ident, pers))
        pers.annenPart?.ident?.let { personIdentFooRepository.save(PersonIdentFoo(it, pers)) }
    }

    fun findById(id: String): Personopplysninger? {
        return personIdentFooRepository.findById(id)
            ?.personopplysninger
    }
}
