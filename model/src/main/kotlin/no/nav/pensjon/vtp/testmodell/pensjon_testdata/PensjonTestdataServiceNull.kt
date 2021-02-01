package no.nav.pensjon.vtp.testmodell.pensjon_testdata

import no.nav.pensjon.vtp.testmodell.repo.Testscenario

class PensjonTestdataServiceNull : PensjonTestdataService {
    override fun opprettData(testscenario: Testscenario) {}
    override fun opprettTestdataScenario(dto: Testscenario, caseId: String): String { TODO("Not yet implemented") }
    override fun hentScenarios(): List<PensjonTestdataScenario> { TODO("Not yet implemented") }
}
