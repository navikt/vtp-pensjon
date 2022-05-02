package no.nav.pensjon.vtp.client

import com.nimbusds.jwt.JWTParser
import no.nav.pensjon.vtp.VtpPensjonApplication
import no.nav.pensjon.vtp.client.unleash.UnleashTestUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest(
    classes = [VtpPensjonApplication::class],
    webEnvironment = RANDOM_PORT,
)
@TestPropertySource(
    properties = [
        "ldap.server.enabled=false"
    ]
)
@Testcontainers
@ContextConfiguration(
    initializers = [
        VtpPensjonClientTest.TestContainerPropertiesInitializer::class
    ],
)
class VtpPensjonClientTest constructor(
    @LocalServerPort serverPort: Int
) {
    companion object {
        @Container
        val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
    }

    class TestContainerPropertiesInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            mongoDBContainer.start()
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                configurableApplicationContext.environment,
                "spring.data.mongodb.port=${mongoDBContainer.firstMappedPort}",
                "pensjon.testdata.url="
            )
        }
    }

    private val vtpPensjonBaseUrl = "http://localhost:$serverPort"

    private val vtpPensjon = VtpPensjonClient(vtpPensjonBaseUrl)
    private val unleashTestUtil = UnleashTestUtil(vtpPensjonBaseUrl)

    @Test
    fun `client can create AzureAD On Behalf Of tokens`() {
        val issuer = "http://vtp-pensjon.local/azureAD"
        val audience = "myAudience"

        val token = vtpPensjon.azureAdOboToken(
            issuer = issuer,
            audience = audience,
            groups = listOf("MY-OWN-GROUP"),
        )

        assertNotNull(token)
        assertNotNull(token.username)

        JWTParser.parse(token.tokenResponse.accessToken).run {
            assertEquals(issuer, jwtClaimsSet.issuer)
            assertTrue(jwtClaimsSet.audience.contains(audience))
        }
    }

    @Test
    fun `client can create AzureAD Client Credentials tokens`() {
        val audience = "myAudience"

        val token = vtpPensjon.azureAdCcToken(audience)

        assertNotNull(token)

        JWTParser.parse(token.tokenResponse.accessToken).run {
            assertTrue(jwtClaimsSet.issuer.contains("/vtp-pensjon/v2.0"))
            assertTrue(jwtClaimsSet.audience.contains(audience))
            assertTrue((jwtClaimsSet.getClaim("roles") as List<*>).contains("access_as_application"))
        }
    }

    @Test
    fun `client can create ISSO tokens`() {
        val issuer = "http://vtp-pensjon.local/isso"
        val audience = "myAudience"

        val token = vtpPensjon.issoToken(
            issuer = issuer,
            clientId = audience,
            groups = listOf("MY-OWN-GROUP"),
        )

        assertNotNull(token)
        assertNotNull(token.username)

        JWTParser.parse(token.tokenResponse.accessToken).run {
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

        JWTParser.parse(token.tokenResponse.accessToken).run {
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
        assertNull(token.username)

        JWTParser.parse(token.tokenResponse.accessToken).run {
            assertEquals(issuer, jwtClaimsSet.issuer)
        }
    }

    @Test
    fun `client can create TokenX tokens`() {
        val audience = "testAudience"

        val token = vtpPensjon.tokenXToken(
            clientAssertion = "testClientAssertion",
            subjectToken = "testSubjectToken",
            audience = audience
        )

        assertNotNull(token)

        JWTParser.parse(token.tokenResponse.accessToken).run {
            assertTrue(jwtClaimsSet.audience.contains(audience))
            assertTrue(jwtClaimsSet.issuer.contains("/rest/tokenx"))
        }
    }

    @Test
    fun `client can enable enable and disable toggle`() {
        val toggleName = "testToggle"

        vtpPensjon.enableToggle(toggleName)
        assertTrue(unleashTestUtil.isEnabled(toggleName))

        vtpPensjon.disableToggle(toggleName)
        assertFalse(unleashTestUtil.isEnabled(toggleName))
    }

    @Test
    fun `client can initialize scenario`() {
        val samboerScenario = vtpPensjon.samboerScenario()
        assertNotNull(samboerScenario)
        assertNotNull(samboerScenario.personopplysningerDto)
        assertNotNull(samboerScenario.personopplysningerDto.soekerIdent)
        assertTrue(samboerScenario.personopplysningerDto.soekerIdent.length == 11)
        assertNotNull(samboerScenario.personopplysningerDto.annenpartIdent)
        assertTrue(samboerScenario.personopplysningerDto.annenpartIdent.length == 11)
    }
}
