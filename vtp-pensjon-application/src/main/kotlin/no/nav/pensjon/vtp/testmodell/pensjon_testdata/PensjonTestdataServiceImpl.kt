package no.nav.pensjon.vtp.testmodell.pensjon_testdata

import no.nav.pensjon.vtp.testmodell.repo.Testscenario
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import org.springframework.web.client.postForObject
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

    override fun opprettTestdataScenario(dto: Testscenario, caseId: String): String? {
        return hentScenarios()
            .filter { i -> i.id.equals(caseId) }
            .map { i -> opprettRequestMedhandlebars(i, dto) }
            .map { i -> client.postForObject<String>("$baseUrl/api/testdata", i) }
            .firstOrNull()
    }

    private fun opprettRequestMedhandlebars(testdataScenario: PensjonTestdataScenario, dto: Testscenario): OpprettPensjonTestdata {
        val handlebars: MutableMap<String, String> = HashMap()
        handlebars.put(testdataScenario.handlebars?.get(0)?.handlebar.orEmpty(), dto.personopplysninger.søker.ident)
        if (testdataScenario.handlebars?.size == 2 && dto.personopplysninger.annenPart != null) {
            handlebars.put(testdataScenario.handlebars.get(1).handlebar.orEmpty(), dto.personopplysninger.annenPart.ident)
        }
        return OpprettPensjonTestdata(testdataScenario.id, handlebars)
    }

    override fun hentScenarios(): List<PensjonTestdataScenario> {
        val url = "$baseUrl/api/testdata/comprehensive"
        val response = client.getForEntity(url, Array<PensjonTestdataScenario>::class.java)
        if (!response.statusCode.is2xxSuccessful || response.body == null) {
            logger.error(
                "Failed to load pensjon-testdata scenarios at uri={}, responseCode={}",
                url,
                response.statusCode
            )
            throw RuntimeException("Failed to load pensjon-testdata scenarios at uri=$url")
        }
        return response.body?.asList() ?: throw RuntimeException("Failed to load pensjon-testdata scenarios at uri=$url")
    }
}
