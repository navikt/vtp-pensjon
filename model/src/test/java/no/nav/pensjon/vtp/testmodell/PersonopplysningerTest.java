package no.nav.pensjon.vtp.testmodell;

import static java.nio.charset.StandardCharsets.UTF_8;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.BarnModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.BrukerModell.Kjønn;
import no.nav.pensjon.vtp.testmodell.personopplysning.Landkode;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell.Diskresjonskoder;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.SivilstandModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.StatsborgerskapModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.impl.StringTestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTilTemplateMapper;
import no.nav.pensjon.vtp.testmodell.util.JsonMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class PersonopplysningerTest {

    @Test
    public void skal_skrive_scenario_til_personopplysninger_json() throws Exception {
        AdresseIndeks adresseIndeks = BasisdataProviderFileImpl.loadAdresser();
        IdenterIndeks identerIndeks = new IdenterIndeks();
        TestscenarioRepository testscenarioRepository = new TestscenarioRepositoryImpl();

        TestscenarioTilTemplateMapper mapper = new TestscenarioTilTemplateMapper();

        Testscenario scenario = new Testscenario("test", "test-1", identerIndeks.getIdenter("test-1"));
        JsonMapper jsonMapper =  new JsonMapper(scenario.getVariabelContainer());
        String lokalIdent = "#id1#";
        SøkerModell søker = new SøkerModell(lokalIdent, "Donald", LocalDate.now().minusYears(20), Kjønn.M);
        Personopplysninger personopplysninger = new Personopplysninger(søker);
        scenario.setPersonopplysninger(personopplysninger);
        søker.setStatsborgerskap(new StatsborgerskapModell(Landkode.NOR));
        søker.setSivilstand(new SivilstandModell("GIFT"));
        søker.setDiskresjonskode(Diskresjonskoder.UDEF);
        søker.setSpråk("nb-NO");
        MedlemskapperiodeModell medlemskapperiode = new MedlemskapperiodeModell();
        søker.leggTil(medlemskapperiode);

        BarnModell barn = new BarnModell("#id2#", "Ole", LocalDate.now());
        personopplysninger.leggTilBarn(barn);

        // Act - writeout
        String json = skrivPersonopplysninger(scenario, mapper, jsonMapper);
        System.out.println("TestscenarioImpl:" + scenario + "\n--------------");
        System.out.println(json);
        System.out.println("--------------");

        // Act - readback

        TestscenarioFraTemplateMapper readMapper = new TestscenarioFraTemplateMapper(adresseIndeks, identerIndeks);
        Testscenario scenario2 = readMapper.lagTestscenario(new StringTestscenarioTemplate("my-template", json, null, null));
        testscenarioRepository.save(scenario2);

        // Assert
        SøkerModell søker2 = scenario2.getPersonopplysninger().getSøker();
        assertThat(søker2).isNotNull();
        assertThat(søker2.getEtternavn()).isNotEmpty();
    }

    private String skrivPersonopplysninger(Testscenario scenario, TestscenarioTilTemplateMapper mapper, JsonMapper jsonMapper) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream buf = new BufferedOutputStream(baos);
        mapper.skrivPersonopplysninger(jsonMapper.canonicalMapper(), buf, scenario);
        buf.flush();
        return baos.toString(UTF_8);
    }
}
