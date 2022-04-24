package no.nav.pensjon.vtp.client

import com.nimbusds.jwt.JWTParser
import no.nav.pensjon.vtp.VtpPensjonApplication
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource

@SpringBootTest(
    classes = [VtpPensjonApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(
    properties = [
        "ldap.server.enabled=false"
    ]
)
class VtpPensjonClientTest constructor(
    @LocalServerPort serverPort: Int
) {
    private val vtpPensjon = VtpPensjonClient("http://localhost:$serverPort")

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
            assertTrue(jwtClaimsSet.audience.contains(audience))
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
