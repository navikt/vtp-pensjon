package no.nav.pensjon.vtp.application.configuration;

import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadOrganisasjoner;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository;

@Component
public class DataLoader {
    private final OrganisasjonRepository organisasjonRepository;

    public DataLoader(OrganisasjonRepository organisasjonRepository) {
        this.organisasjonRepository = organisasjonRepository;
    }

    @PostConstruct
    public void loadData() throws IOException {
        loadOrganisasjoner(organisasjonRepository);
    }
}
