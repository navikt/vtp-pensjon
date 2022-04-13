package no.nav.pensjon.vtp.client

import no.nav.pensjon.vtp.client.testcontainers.VtpPensjonContainer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class VtpPensjonClientTest {
    companion object {
        @Container
        val vtpPensjonContainer: VtpPensjonContainer = VtpPensjonContainer()
            .withEmbeddedMongoDB()
            .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger(VtpPensjonContainer::class.java)))
    }

    @Test
    fun `basic values are configured`() {
        assertNotNull(vtpPensjonContainer.baseUrl())
        assertNotNull(vtpPensjonContainer.ldapUrl())
    }

    @Test
    fun `client can create sts tokens`() {
        val vtpPensjon = vtpPensjonContainer.client()

        val stsToken = vtpPensjon.stsToken(
            issuer = "http://vtp-pensjon.local/sts",
            user = "srv",
        )

        assertNotNull(stsToken)
        assertEquals("srv", stsToken.username)
    }
}
