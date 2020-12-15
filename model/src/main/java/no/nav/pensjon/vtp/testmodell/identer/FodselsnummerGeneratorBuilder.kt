package no.nav.pensjon.vtp.testmodell.identer

import no.nav.pensjon.vtp.testmodell.enums.IdentType
import no.nav.pensjon.vtp.testmodell.enums.Kjonn
import no.nav.pensjon.vtp.testmodell.util.TestdataUtil.generateRandomPlausibleBirtdayParent
import java.time.LocalDate

class FodselsnummerGeneratorBuilder {
    var kjonn: Kjonn = Kjonn.randomKjonn()
    var identType: IdentType = IdentType.FNR
    var fodselsdato: LocalDate = generateRandomPlausibleBirtdayParent()

    fun kjonn(k: Kjonn): FodselsnummerGeneratorBuilder {
        kjonn = k
        return this
    }

    fun identType(i: IdentType): FodselsnummerGeneratorBuilder {
        identType = i
        return this
    }

    fun fodselsdato(lt: LocalDate?): FodselsnummerGeneratorBuilder {
        lt?.let { fodselsdato = it }
        return this
    }

    fun buildAndGenerate(): String {
        return FoedselsnummerGenerator(kjonn, identType, fodselsdato).generate()
    }
}
