package no.nav.pensjon.vtp.testmodell

import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType
import no.nav.pensjon.vtp.testmodell.personopplysning.Landkode
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AdresserTest {
    @Test
    fun sjekk_scenarios() {
        sjekkAdresseIndeks(BasisdataProviderFileImpl.loadAdresser())
    }

    private fun sjekkAdresseIndeks(sc: AdresseIndeks) {
        val bostedsadresse = sc.finn(AdresseType.BOSTEDSADRESSE, Landkode.NOR)
        Assertions.assertThat(bostedsadresse).isNotNull()
        Assertions.assertThat(sc.finn(AdresseType.MIDLERTIDIG_POSTADRESSE, Landkode.NOR)).isNotNull()
        Assertions.assertThat(sc.finn(AdresseType.MIDLERTIDIG_POSTADRESSE, Landkode.USA)).isNotNull()
        Assertions.assertThat(sc.finn(AdresseType.POSTADRESSE, Landkode.NOR)).isNotNull()
    }
}
