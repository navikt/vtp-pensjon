package no.nav.pensjon.vtp.mocks.saf

import graphql.ExecutionInput
import graphql.GraphQL
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.mocks.saf.dokument.DokumentService
import no.nav.pensjon.vtp.mocks.saf.graphql.GraphQLRequest
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "saf")
@RequestMapping("/api/saf")
class SafMock(@Qualifier("graphQLSaf") private val graphQlSaf: GraphQL, private val dokumentService: DokumentService) {


    @PostMapping(
        path = ["", "/graphql"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun graphQLRequest(
        @RequestHeader(AUTHORIZATION) authorizationHeader: String?,
        @RequestHeader(
            X_CORRELATION_ID
        ) xCorrelationId: String?,
        @RequestHeader(
            NAV_CALLID
        ) navCallid: String?,
        @RequestHeader(
            NAV_CONSUMER_ID
        ) navConsumerId: String?,
        @RequestBody
        request: GraphQLRequest
    ): MutableMap<String, Any> =
        ExecutionInput.newExecutionInput()
            .query(request.query)
            .variables(request.variables)
            .build()
            .let { graphQlSaf.execute(it) }
            .toSpecification()


    @GetMapping("/rest/hentdokument/{journalpostId}/{dokumentInfoId}/{variantFormat}")
    fun hentDokument(
        @PathVariable(
            JOURNALPOST_ID
        ) journalpostId: String?,
        @PathVariable(
            DOKUMENT_INFO_ID
        ) dokumentInfoId: String,
        @PathVariable(
            VARIANT_FORMAT
        ) variantFormat: String
    ): ResponseEntity<Any> =
        ResponseEntity
            .status(HttpStatus.OK)
            .header(CONTENT_DISPOSITION, "inline; filename=" + dokumentInfoId + "_" + variantFormat)
            .body(dokumentService.hentDokument(journalpostId, dokumentInfoId, variantFormat))

    companion object {
        private const val X_CORRELATION_ID = "X-Correlation-ID"
        private const val NAV_CALLID = "Nav-Callid"
        private const val NAV_CONSUMER_ID = "Nav-Consumer-Id"
        private const val JOURNALPOST_ID = "journalpostId"
        private const val DOKUMENT_INFO_ID = "dokumentInfoId"
        private const val VARIANT_FORMAT = "variantFormat"
    }
}