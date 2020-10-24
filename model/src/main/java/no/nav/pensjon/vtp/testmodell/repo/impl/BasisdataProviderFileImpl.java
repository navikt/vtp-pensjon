package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;
import no.nav.pensjon.vtp.testmodell.identer.FiktiveFnr;
import no.nav.pensjon.vtp.testmodell.identer.IdentGenerator;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.pensjon.vtp.testmodell.repo.BasisdataProvider;
import no.nav.pensjon.vtp.testmodell.util.JsonMapper;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetIndeks;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetModell;

public class BasisdataProviderFileImpl implements BasisdataProvider {

    private final VirksomhetIndeks virksomhetIndeks = new VirksomhetIndeks();
    private final EnheterIndeks enheterIndeks = new EnheterIndeks();
    private final AnsatteIndeks ansatteIndeks = new AnsatteIndeks();
    private final AdresseIndeks adresseIndeks = new AdresseIndeks();
    private final OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();
    private final IdentGenerator identGenerator = new FiktiveFnr();

    private final JsonMapper jsonMapper = new JsonMapper();

    public BasisdataProviderFileImpl() throws IOException{
        loadAdresser();
        loadEnheter();
        loadAnsatte();
        loadVirksomheter();
        loadOrganisasjoner();
    }

    @Override
    public VirksomhetIndeks getVirksomhetIndeks() {
        return virksomhetIndeks;
    }

    @Override
    public EnheterIndeks getEnheterIndeks() {
        return enheterIndeks;
    }

    @Override
    public AnsatteIndeks getAnsatteIndeks() {
        return ansatteIndeks;
    }

    @Override
    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }

    @Override
    public IdentGenerator getIdentGenerator() {
        return identGenerator;
    }

    private void loadAdresser() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/adresse-maler.json")) {
            TypeReference<List<AdresseModell>> typeRef = new TypeReference<>() {
            };
            List<AdresseModell> adresser = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            adresser.forEach(adresseIndeks::leggTil);
        }
    }

    private void loadEnheter() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/enheter.json")) {
            TypeReference<List<Norg2Modell>> typeRef = new TypeReference<>() {
            };
            List<Norg2Modell> adresser = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            enheterIndeks.leggTil(adresser);
        }
    }

    private void loadAnsatte() {
        try (InputStream is = AnsatteIndeks.class.getResourceAsStream("/basedata/navansatte.json")) {
            TypeReference<List<NAVAnsatt>> typeRef = new TypeReference<>() {
            };
            final JsonMapper jsonMapper = new JsonMapper();
            List<NAVAnsatt> ansatte = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            ansatteIndeks.leggTil(ansatte);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadVirksomheter() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/virksomheter.json")) {
            TypeReference<List<VirksomhetModell>> typeRef = new TypeReference<>() {
            };
            List<VirksomhetModell> virksomheter = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            virksomhetIndeks.leggTil(virksomheter);
        }
    }

    private void loadOrganisasjoner() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/organisasjon.json")) {
            TypeReference<List<OrganisasjonModell>> typeRef = new TypeReference<>() {
            };
            List<OrganisasjonModell> organisasjoner = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            organisasjonIndeks.leggTil(organisasjoner);
        }
    }
}
