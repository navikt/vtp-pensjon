package no.nav.pensjon.vtp.testmodell.identer

import no.nav.pensjon.vtp.testmodell.util.TestdataUtil.generateBirtdayBetweenYears
import no.nav.pensjon.vtp.testmodell.util.TestdataUtil.generateBirthdateNowMinusThreeYears
import no.nav.pensjon.vtp.testmodell.util.TestdataUtil.generateRandomPlausibleBirtdayParent
import no.nav.pensjon.vtp.testmodell.enums.IdentType.DNR
import no.nav.pensjon.vtp.testmodell.enums.Kjonn.KVINNE
import no.nav.pensjon.vtp.testmodell.enums.Kjonn.MANN
import no.nav.pensjon.vtp.testmodell.util.JsonMapper.Companion.parseLocalDateOrNull

/**
 * Hent et tilfeldig gyldig men fiktivt Fødselsnummer.
 *
 * @see <a href="https://confluence.adeo.no/pages/viewpageattachments.action?pageId=211653415&metadataLink=true">https://confluence.adeo.no/pages/viewpageattachments.action?pageId=211653415&metadataLink=true</a>
 */
class FiktiveFnr : IdentGenerator {
    /**
     * Bruk denne når kjønn ikke har betydning for anvendt FNR. (Bør normalt brukes slik at en sikrer at applikasjonen ikke gjør antagelser om
     * koding av kjønn i FNR.
     */
    override fun tilfeldigFnr(): String {
        return FodselsnummerGeneratorBuilder()
            .fodselsdato(generateBirtdayBetweenYears(1911, 1940))
            .buildAndGenerate()
    }

    override fun tilfeldigMannFnr(foedselsdato: String?): String {
        return FodselsnummerGeneratorBuilder()
            .kjonn(MANN)
            .fodselsdato(parseLocalDateOrNull(foedselsdato))
            .buildAndGenerate()
    }

    override fun tilfeldigKvinneFnr(foedselsdato: String?): String {
        return FodselsnummerGeneratorBuilder()
            .kjonn(KVINNE)
            .fodselsdato(parseLocalDateOrNull(foedselsdato))
            .buildAndGenerate()
    }

    /** Returnerer FNR for barn (tilfeldig kjønn) < 3 år  */
    override fun tilfeldigBarnUnderTreAarFnr(): String {
        return FodselsnummerGeneratorBuilder()
            .fodselsdato(generateBirthdateNowMinusThreeYears())
            .buildAndGenerate()
    }

    /** Returnerer DNR for kvinne > 18 år  */
    override fun tilfeldigKvinneDnr(): String {
        return FodselsnummerGeneratorBuilder()
            .fodselsdato(generateRandomPlausibleBirtdayParent())
            .identType(DNR)
            .buildAndGenerate()
    }
}
