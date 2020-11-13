package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.pensjon.vtp.testmodell.util.JsonMapper;

public class BasisdataProviderFileImpl {
    public static AdresseIndeks loadAdresser() throws IOException {
        final JsonMapper jsonMapper = new JsonMapper();

        final AdresseIndeks adresseIndeks = new AdresseIndeks();

        try (InputStream is = BasisdataProviderFileImpl.class.getResourceAsStream("/basedata/adresse-maler.json")) {
            TypeReference<List<AdresseModell>> typeRef = new TypeReference<>() {
            };
            List<AdresseModell> adresser = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            adresser.forEach(adresseIndeks::leggTil);
        }

        return adresseIndeks;
    }

    public static EnheterIndeks loadEnheter() throws IOException {
        final JsonMapper jsonMapper = new JsonMapper();

        final EnheterIndeks enheterIndeks = new EnheterIndeks();
        try (InputStream is = BasisdataProviderFileImpl.class.getResourceAsStream("/basedata/enheter.json")) {
            TypeReference<List<Norg2Modell>> typeRef = new TypeReference<>() {
            };
            List<Norg2Modell> adresser = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            enheterIndeks.leggTil(adresser);
        }

        return enheterIndeks;
    }

    public static AnsatteIndeks loadAnsatte() {
        final AnsatteIndeks ansatteIndeks = new AnsatteIndeks();

        try (InputStream is = AnsatteIndeks.class.getResourceAsStream("/basedata/navansatte.json")) {
            TypeReference<List<NAVAnsatt>> typeRef = new TypeReference<>() {
            };
            final JsonMapper jsonMapper = new JsonMapper();
            List<NAVAnsatt> ansatte = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            ansatteIndeks.leggTil(ansatte);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ansatteIndeks;
    }

    public static void loadOrganisasjoner(final OrganisasjonRepository organisasjonRepository) throws IOException {
        final JsonMapper jsonMapper = new JsonMapper();
        try (InputStream is = BasisdataProviderFileImpl.class.getResourceAsStream("/basedata/organisasjon.json")) {
            TypeReference<List<OrganisasjonModell>> typeRef = new TypeReference<>() {
            };
            List<OrganisasjonModell> organisasjoner = jsonMapper.lagObjectMapper().readValue(is, typeRef);
            organisasjonRepository.saveAll(organisasjoner);
        }
    }
}
