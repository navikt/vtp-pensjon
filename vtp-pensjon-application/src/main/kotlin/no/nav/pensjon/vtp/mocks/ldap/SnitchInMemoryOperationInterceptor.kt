package no.nav.pensjon.vtp.mocks.ldap

import com.fasterxml.jackson.databind.ObjectMapper
import com.unboundid.ldap.listener.interceptor.*
import no.nav.pensjon.vtp.snitch.Payload
import no.nav.pensjon.vtp.snitch.RequestResponse
import no.nav.pensjon.vtp.snitch.SnitchService
import org.springframework.stereotype.Component

@Component
class SnitchInMemoryOperationInterceptor(private val snitchService: SnitchService) : InMemoryOperationInterceptor() {
    private val objectMapper = ObjectMapper()

    override fun processAddResult(result: InMemoryInterceptedAddResult?) {
        snitchService.save(requestResponse("ad", emptyPayload(), emptyPayload()))
    }

    override fun processSASLBindResult(result: InMemoryInterceptedSASLBindResult?) {
        snitchService.save(requestResponse("saslBin", emptyPayload(), emptyPayload()))
    }

    override fun processCompareResult(result: InMemoryInterceptedCompareResult?) {
        snitchService.save(requestResponse("compare", emptyPayload(), emptyPayload()))
    }

    override fun processDeleteResult(result: InMemoryInterceptedDeleteResult?) {
        snitchService.save(requestResponse("delete", emptyPayload(), emptyPayload()))
    }

    override fun processExtendedResult(result: InMemoryInterceptedExtendedResult?) {
        snitchService.save(requestResponse("extended", emptyPayload(), emptyPayload()))
    }

    override fun processModifyResult(result: InMemoryInterceptedModifyResult?) {
        snitchService.save(requestResponse("modify", emptyPayload(), emptyPayload()))
    }

    override fun processModifyDNResult(result: InMemoryInterceptedModifyDNResult?) {
        snitchService.save(requestResponse("modifyDN", emptyPayload(), emptyPayload()))
    }

    override fun processSearchRequest(request: InMemoryInterceptedSearchRequest?) {
        super.processSearchRequest(request)
    }

    override fun processSearchResult(result: InMemoryInterceptedSearchResult?) {
        snitchService.save(
            requestResponse(
                "search",
                payload(result?.request?.run {
                    mapOf(
                        "attributeList" to attributeList,
                        "baseDN" to baseDN,
                        "dereferencePolicy" to dereferencePolicy,
                        "filter" to filter,
                        "scope" to scope,
                        "sizeLimit" to sizeLimit,
                        "timeLimitSeconds" to timeLimitSeconds,
                        "controlList" to controlList,
                        "controls" to controls,
                    )
                }),
                payload(result?.result?.run {
                    mapOf(
                        "diagnosticMessage" to diagnosticMessage,
                        "matchedDN" to matchedDN ,
                        "responseControls" to responseControls,
                        "messageID" to messageID,
                        "referralURLs" to referralURLs,
                    )
                }),
            )
        )
    }

    override fun processIntermediateResponse(response: InMemoryInterceptedIntermediateResponse?) {
        snitchService.save(requestResponse("intermediate", emptyPayload(), emptyPayload()))
    }

    override fun processSimpleBindResult(result: InMemoryInterceptedSimpleBindResult?) {
        snitchService.save(
            requestResponse("bind", payload(
                result?.request?.run {
                    mapOf(
                        "bindDn" to bindDN,
                        "password" to password
                    )
                }), payload(result?.result?.run {
                mapOf(
                    "diagnosticMessage" to diagnosticMessage,
                    "matchedDN" to matchedDN,
                    "responseControls" to responseControls,
                    "resultCode" to resultCode,
                    "referralURLs" to referralURLs,
                    "matchedDN" to matchedDN,
                )
            })
            )
        )
    }

    private fun requestResponse(
        path: String,
        request: Payload,
        responsePayload: Payload
    ) = RequestResponse(
        method = "LDAP",
        path = path,
        url = "ldap://",
        status = 0,
        request = request,
        response = responsePayload
    )

    private fun payload(content: Any?) =
        content?.let { jsonPayload(objectMapper.writeValueAsBytes(it)) } ?: emptyPayload()

    private fun emptyPayload() = Payload(
        headers = emptyMap(),
        contentType = null,
        contentLength = 0,
        content = null,
    )

    private fun jsonPayload(content: ByteArray) = Payload(
        headers = emptyMap(),
        contentType = "application/json",
        contentLength = content.size,
        content = content,
    )
}

