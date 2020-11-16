package no.nav.pensjon.vtp.testmodell.repo.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TestscenarioTemplateLoaderTest {
    @Test
    public void verify_that_all_scenarios_loads() {
        assertThat(new TestscenarioTemplateLoader().load())
                .isNotEmpty();
    }
}
