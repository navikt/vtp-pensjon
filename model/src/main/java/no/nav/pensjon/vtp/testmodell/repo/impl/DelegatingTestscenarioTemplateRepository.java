package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.util.Collection;
import java.util.Objects;

import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;

public class DelegatingTestscenarioTemplateRepository implements TestscenarioTemplateRepository {

    private volatile TestscenarioTemplateRepository delegate;

    public DelegatingTestscenarioTemplateRepository(TestscenarioTemplateRepository delegate) {
        Objects.requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public Collection<TestscenarioTemplate> getTemplates() {
        return delegate.getTemplates();
    }

    @Override
    public TestscenarioTemplate finn(String templateKey) {
       return delegate.finn(templateKey);
    }

    @Override
    public TestscenarioTemplate finnMedTemplatenavn(String templateNavn) {
        return delegate.finnMedTemplatenavn(templateNavn);
    }


}
