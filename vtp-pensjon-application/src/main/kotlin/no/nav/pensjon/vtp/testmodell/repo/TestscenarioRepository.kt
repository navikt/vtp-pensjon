package no.nav.pensjon.vtp.testmodell.repo

import org.springframework.data.repository.Repository

interface TestscenarioRepository : Repository<Testscenario, String> {
    fun findAll(): List<Testscenario>
    fun findById(id: String): Testscenario?
    fun deleteById(id: String)
    fun save(testscenario: Testscenario): Testscenario
}
