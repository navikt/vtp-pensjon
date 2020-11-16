package no.nav.pensjon.vtp.testmodell.personopplysning

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PersonIdentFoo(
        @Id
        val ident: String,
        val personopplysninger: Personopplysninger
)
