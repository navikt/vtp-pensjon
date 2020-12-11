package no.nav.pensjon.vtp.testmodell.pensjon_testdata

import org.springframework.web.client.RestTemplate
import no.nav.pensjon.vtp.testmodell.repo.Testscenario
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import java.lang.RuntimeException

class PensjonTestdataServiceImpl(private val baseUrl: String) : PensjonTestdataService {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val client = RestTemplate()

    override fun opprettData(testscenario: Testscenario) {
        lagrePerson(testscenario.personopplysninger.søker.ident)
        testscenario.personopplysninger.annenPart?.let { lagrePerson(it.ident) }
    }

    fun lagrePerson(ident: String) {
        val url = "$baseUrl/api/person/$ident"
        val request = HttpEntity<Any>(null, HttpHeaders())
        val response = client.postForEntity(url, request, String::class.java)
        if (!response.statusCode.is2xxSuccessful) {
            logger.error(
                "Failed to create person (ident={}, uri={}, responseCode={})",
                ident,
                baseUrl,
                response.statusCode
            )
            throw RuntimeException("Failed to create person with ident=$ident in pensjon-testdata using Uri=$baseUrl")
        }
    }
}
