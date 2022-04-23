package no.nav.pensjon.vtp.client

import com.nimbusds.jwt.JWTParser
import no.nav.pensjon.vtp.client.testcontainers.VtpPensjonContainer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class VtpPensjonClientTest2 {
    companion object {
        @Container
        val vtpPensjonContainer: VtpPensjonContainer = VtpPensjonContainer()
            .withEmbeddedMongoDB()
            .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger(VtpPensjonContainer::class.java)))
    }

    private val vtpPensjon = vtpPensjonContainer.client()

    @Test
    fun `basic values are configured`() {
        assertNotNull(vtpPensjonContainer.baseUrl())
        assertNotNull(vtpPensjonContainer.ldapUrl())
    }

    @Test
    fun `client can create AzureAD On Behalf Of tokens`() {
        val issuer = "http://vtp-pensjon.local/azureAD"
        val audience = "myAudience"

        val token = vtpPensjon.azureAdOboToken(
            issuer = issuer,
            audience = audience,
            groups = listOf("MY-OWN-GROUP")
        )

        assertNotNull(token)
        assertNotNull(token.username)

        JWTParser.parse(token.token).run {
            assertEquals(issuer, jwtClaimsSet.issuer)
            assertEquals(audience, jwtClaimsSet.audience)
        }
    }

    @Test
    fun `client can create STS (Gandalf) tokens`() {
        val issuer = "http://vtp-pensjon.local/sts"

        val token = vtpPensjon.stsToken(
            issuer = issuer,
            user = "srv",
        )

        assertNotNull(token)
        assertEquals("srv", token.username)

        JWTParser.parse(token.token).run {
            assertEquals(issuer, jwtClaimsSet.issuer)
        }
    }

    @Test
    fun `client can create Maskinporten tokens`() {
        val issuer = "http://vtp-pensjon.local/maskinporten"

        val token = vtpPensjon.maskinportenToken(
            issuer = issuer,
            consumer = "testConsumer",
            scope = "testScope",
        )

        assertNotNull(token)
        assertEquals("testConsumer", token.username)

        JWTParser.parse(token.token.access_token).run {
            assertEquals(issuer, jwtClaimsSet.issuer)
        }
    }
}
