package no.nav.pensjon.vtp.testmodell.repo.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BasisdataProviderFileImplTest {
    @Test
    fun loadAnsatte() {
        val ansatte = BasisdataProviderFileImpl.loadAnsatte()

        assertFalse(ansatte.findAll().isEmpty())

    }

}
