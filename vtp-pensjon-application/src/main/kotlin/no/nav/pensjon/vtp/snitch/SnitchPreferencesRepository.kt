package no.nav.pensjon.vtp.snitch

import org.springframework.data.mongodb.repository.MongoRepository

interface SnitchPreferencesRepository : MongoRepository<SnitchPreferences, String>
