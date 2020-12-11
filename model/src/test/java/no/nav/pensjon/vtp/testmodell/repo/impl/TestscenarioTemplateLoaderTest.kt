package no.nav.pensjon.vtp.testmodell.repo.impl

import org.assertj.core.api.Assertions
import org.junit.Test

class TestscenarioTemplateLoaderTest {
    @Test
    fun verify_that_all_scenarios_loads() {
        Assertions.assertThat(TestscenarioTemplateLoader().load())
                .isNotEmpty
    }
}
