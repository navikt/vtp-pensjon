package no.nav.pensjon.vtp.testmodell.organisasjon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class OrganisasjonIndeks {
    private final Map<String, OrganisasjonModell> organisasjoner = new HashMap<>();

    public Optional<OrganisasjonModell> getOrganisasjon(String orgnr) {
        return getModellForIdent(orgnr);
    }

    public synchronized Optional<OrganisasjonModell> getModellForIdent(String orgnr) {
        return Optional.ofNullable(organisasjoner.get(orgnr));
    }

    public synchronized void leggTil(List<OrganisasjonModell> modeller) {
        modeller.forEach(o -> organisasjoner.put(o.getOrgnummer(), o));
    }
}
