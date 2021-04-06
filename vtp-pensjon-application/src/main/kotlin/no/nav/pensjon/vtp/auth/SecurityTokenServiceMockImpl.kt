package no.nav.pensjon.vtp.auth

import org.apache.cxf.ws.security.sts.provider.SecurityTokenService
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenCollectionType
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseCollectionType
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenType
import org.apache.cxf.ws.security.sts.provider.model.wstrust14.ObjectFactory
import javax.jws.*
import javax.jws.soap.SOAPBinding
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.Action
import javax.xml.ws.soap.Addressing

/** Mock implementation of STS service for WS-Trust.  */
@WebService(targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512?wsdl", name = "SecurityTokenService")
@XmlSeeAlso(
    org.apache.cxf.ws.security.sts.provider.model.ObjectFactory::class,
    ObjectFactory::class,
    org.apache.cxf.ws.security.sts.provider.model.secext.ObjectFactory::class,
    org.apache.cxf.ws.security.sts.provider.model.utility.ObjectFactory::class,
    org.apache.cxf.ws.security.sts.provider.model.xmldsig.ObjectFactory::class,
    org.apache.cxf.ws.addressing.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@Addressing
class SecurityTokenServiceMockImpl(private val stsIssueResponseGenerator: STSIssueResponseGenerator) :
    SecurityTokenService {
    @WebResult(
        name = "RequestSecurityTokenResponse",
        targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512",
        partName = "response"
    )
    @Action(
        input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/KET",
        output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/KETFinal"
    )
    @WebMethod(operationName = "KeyExchangeToken", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/KET")
    override fun keyExchangeToken(
        @WebParam(
            partName = "request",
            name = "RequestSecurityToken",
            targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512"
        ) request: RequestSecurityTokenType
    ): RequestSecurityTokenResponseType {
        // TODO - må verifisers dersom behov
        return stsIssueResponseGenerator.buildRequestSecurityTokenResponseType(request)
    }

    @WebResult(
        name = "RequestSecurityTokenResponseCollection",
        targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512",
        partName = "responseCollection"
    )
    @Action(
        input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue",
        output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTRC/IssueFinal"
    )
    @WebMethod(operationName = "Issue", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue")
    override fun issue(
        @WebParam(
            partName = "request",
            name = "RequestSecurityToken",
            targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512"
        ) request: RequestSecurityTokenType
    ): RequestSecurityTokenResponseCollectionType {
        return stsIssueResponseGenerator.buildRequestSecurityTokenResponseCollectionType(request)
    }

    @WebResult(
        name = "RequestSecurityTokenResponse",
        targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512",
        partName = "response"
    )
    @Action(
        input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue",
        output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTRC/IssueFinal"
    )
    @WebMethod(action = "Issue")
    override fun issueSingle(
        @WebParam(
            partName = "request",
            name = "RequestSecurityToken",
            targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512"
        ) request: RequestSecurityTokenType
    ): RequestSecurityTokenResponseType {
        return stsIssueResponseGenerator.buildRequestSecurityTokenResponseType(request)
    }

    @WebResult(
        name = "RequestSecurityTokenResponse",
        targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512",
        partName = "response"
    )
    @Action(
        input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Cancel",
        output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/CancelFinal"
    )
    @WebMethod(operationName = "Cancel", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Cancel")
    override fun cancel(
        @WebParam(
            partName = "request",
            name = "RequestSecurityToken",
            targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512"
        ) request: RequestSecurityTokenType
    ): RequestSecurityTokenResponseType {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @WebResult(
        name = "RequestSecurityTokenResponse",
        targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512",
        partName = "response"
    )
    @Action(
        input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Validate",
        output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/ValidateFinal"
    )
    @WebMethod(operationName = "Validate", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Validate")
    override fun validate(
        @WebParam(
            partName = "request",
            name = "RequestSecurityToken",
            targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512"
        ) request: RequestSecurityTokenType
    ): RequestSecurityTokenResponseType {
        // TODO - må verifiseres dersom behov
        return stsIssueResponseGenerator.buildRequestSecurityTokenResponseType(request)
    }

    @WebResult(
        name = "RequestSecurityTokenResponseCollection",
        targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512",
        partName = "responseCollection"
    )
    @WebMethod(operationName = "RequestCollection")
    override fun requestCollection(
        @WebParam(
            partName = "requestCollection",
            name = "RequestSecurityTokenCollection",
            targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512"
        ) requestCollection: RequestSecurityTokenCollectionType
    ): RequestSecurityTokenResponseCollectionType {
        return stsIssueResponseGenerator.buildRequestSecurityTokenResponseCollectionType(requestCollection)
    }

    @WebResult(
        name = "RequestSecurityTokenResponse",
        targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512",
        partName = "response"
    )
    @Action(
        input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Renew",
        output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/RenewFinal"
    )
    @WebMethod(operationName = "Renew", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Renew")
    override fun renew(
        @WebParam(
            partName = "request",
            name = "RequestSecurityToken",
            targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512"
        ) request: RequestSecurityTokenType
    ): RequestSecurityTokenResponseType {
        return stsIssueResponseGenerator.buildRequestSecurityTokenResponseType(request)
    }
}
