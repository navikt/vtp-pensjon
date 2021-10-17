package no.nav.pensjon.vtp.mocks.tp

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Sequence(
    @Id
    val name: String,
    val value: Long = 0L
)
