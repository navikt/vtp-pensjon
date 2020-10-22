package no.nav.pensjon.vtp.testmodell.identer;

import no.nav.pensjon.vtp.testmodell.enums.IdentType;
import no.nav.pensjon.vtp.testmodell.enums.Kjonn;
import no.nav.pensjon.vtp.testmodell.util.JsonMapper;
import no.nav.pensjon.vtp.testmodell.util.TestdataUtil;

/**
 * Hent et tilfeldig gyldig men fiktivt Fødselsnummer.
 *
 * @see https://confluence.adeo.no/pages/viewpageattachments.action?pageId=211653415&metadataLink=true (SKD fiktive identer)
 */
public class FiktiveFnr implements IdentGenerator {
    /**
     * Bruk denne når kjønn ikke har betydning for anvendt FNR. (Bør normalt brukes slik at en sikrer at applikasjonen ikke gjør antagelser om
     * koding av kjønn i FNR.
     */
    @Override
    public String tilfeldigFnr() {
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .fodselsdato(TestdataUtil.generateBirtdayBetweenYears(1911, 1940))
                .buildAndGenerate();
    }

    @Override
    public String tilfeldigMannFnr(String foedselsdato) {
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .kjonn(Kjonn.MANN)
                .fodselsdato(JsonMapper.parseLocalDate(foedselsdato))
                .buildAndGenerate();
    }

    @Override
    public String tilfeldigKvinneFnr(String foedselsdato) {
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .kjonn(Kjonn.KVINNE)
                .fodselsdato(JsonMapper.parseLocalDate(foedselsdato))
                .buildAndGenerate();
    }

    /** Returnerer FNR for barn (tilfeldig kjønn) < 3 år */
    @Override
    public String tilfeldigBarnUnderTreAarFnr() {
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .fodselsdato(TestdataUtil.generateBirthdateNowMinusThreeYears())
                .buildAndGenerate();
    }

    /** Returnerer DNR for kvinne > 18 år */
    @Override
    public String tilfeldigKvinneDnr() {
        return new FoedselsnummerGenerator
                .FodselsnummerGeneratorBuilder()
                .fodselsdato(TestdataUtil.generateRandomPlausibleBirtdayParent())
                .identType(IdentType.DNR)
                .buildAndGenerate();
    }


}
