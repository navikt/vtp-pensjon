package no.nav.pensjon.vtp.testmodell.pensjon_testdata

import no.nav.pensjon.vtp.testmodell.repo.Testscenario

interface PensjonTestdataService {
    fun opprettData(testscenario: Testscenario)
    fun opprettTestdataScenario(dto: Testscenario, caseId: String): String?
    fun hentScenarios(): List<PensjonTestdataScenario>
}
