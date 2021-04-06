package no.nav.pensjon.vtp.auth

import no.nav.pensjon.vtp.felles.CryptoConfigurationParameters
import org.apache.cxf.security.SecurityContext
import org.apache.cxf.sts.StaticSTSProperties
import org.apache.cxf.sts.operation.TokenIssueOperation
import org.apache.cxf.sts.request.ReceivedToken
import org.apache.cxf.sts.request.RequestRequirements
import org.apache.cxf.sts.token.delegation.UsernameTokenDelegationHandler
import org.apache.cxf.sts.token.provider.SAMLTokenProvider
import org.apache.cxf.sts.token.provider.TokenProviderParameters
import org.apache.cxf.ws.security.sts.provider.model.*
import org.apache.wss4j.common.crypto.Crypto
import org.apache.wss4j.common.ext.WSPasswordCallback
import org.apache.wss4j.common.principal.CustomTokenPrincipal
import org.springframework.stereotype.Component
import java.security.Principal
import java.util.*
import javax.security.auth.callback.Callback
import javax.security.auth.callback.CallbackHandler

@Component
class STSIssueResponseGenerator(crypto: Crypto, private val cryptoConfiguration: CryptoConfigurationParameters) {
    private val issueOperation: TokenIssueOperation

    internal class PasswordCallbackHandler(private val cryptoConfigurationParameters: CryptoConfigurationParameters) :
        CallbackHandler {
        override fun handle(callbacks: Array<Callback>) {
            for (callback in callbacks) {
                if (callback is WSPasswordCallback) { // CXF
                    if (cryptoConfigurationParameters.keyAndCertAlias == callback.identifier) {
                        callback.password = cryptoConfigurationParameters.keystorePassword
                        break
                    }
                }
            }
        }
    }

    private fun createMessageContext(principal: Principal): Map<String, Any> {
        val messageContext: MutableMap<String, Any> = HashMap()
        messageContext[SecurityContext::class.java.name] = createSecurityContext(principal)
        return messageContext
    }

    fun buildRequestSecurityTokenResponseCollectionType(requestCollection: RequestSecurityTokenCollectionType?): RequestSecurityTokenResponseCollectionType {
        val principal: Principal = CustomTokenPrincipal(USERNAME)
        val messageContext = createMessageContext(principal)
        return issueOperation.issue(requestCollection, principal, messageContext)
    }

    /** Issue a token as part of collection  */
    fun buildRequestSecurityTokenResponseCollectionType(request: RequestSecurityTokenType?): RequestSecurityTokenResponseCollectionType {
        val principal: Principal = CustomTokenPrincipal(USERNAME)
        val messageContext = createMessageContext(principal)
        return issueOperation.issue(request, principal, messageContext)
    }

    /** Issue a single token  */
    fun buildRequestSecurityTokenResponseType(request: RequestSecurityTokenType?): RequestSecurityTokenResponseType {
        val principal: Principal = CustomTokenPrincipal(USERNAME)
        val messageContext = createMessageContext(principal)
        return issueOperation.issueSingle(request, principal, messageContext)
    }

    /** Issue a single token  */
    fun buildRequestSecurityTokenResponseType(tokenType: String?): RequestSecurityTokenResponseType {
        val of = ObjectFactory()
        val request2 = of.createRequestSecurityTokenType()
        request2.any.add(of.createTokenType(tokenType))
        request2.any.add(of.createRequestType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue"))
        val principal: Principal = CustomTokenPrincipal(USERNAME)
        val messageContext = createMessageContext(principal)
        return issueOperation.issueSingle(request2, principal, messageContext)
    }

    private fun initSTSProperties(crypto: Crypto) = StaticSTSProperties().apply {
        encryptionCrypto = crypto
        signatureCrypto = crypto
        callbackHandler = PasswordCallbackHandler(
            cryptoConfiguration
        )
        signatureUsername = cryptoConfiguration.keyAndCertAlias
        issuer = "VTP"
    }

    companion object {
        private const val USERNAME = "goddagmannøkseskaft"
        private fun createIssueOperation(staticSTSProperties: StaticSTSProperties): TokenIssueOperation {
            val tokenIssueOperation: TokenIssueOperation = object : TokenIssueOperation() {
                override fun createTokenProviderParameters(
                    requestRequirements: RequestRequirements,
                    principal: Principal,
                    messageContext: Map<String, Any>
                ): TokenProviderParameters {
                    val providerParameters =
                        super.createTokenProviderParameters(requestRequirements, principal, messageContext)
                    val tokenRequirements = providerParameters.tokenRequirements
                    tokenRequirements.onBehalfOf = null
                    tokenRequirements.actAs = null
                    return providerParameters
                }
            }

            // Add Token Provider
            tokenIssueOperation.tokenProviders = listOf(SAMLTokenProvider())

            // Add TokenDelegationHandler for onBehalfOf
            tokenIssueOperation.delegationHandlers = listOf(
                object : UsernameTokenDelegationHandler() {
                    override fun canHandleToken(delegateTarget: ReceivedToken): Boolean {
                        return true
                    }
                }
            )
            tokenIssueOperation.setStsProperties(staticSTSProperties)
            return tokenIssueOperation
        }

        private fun createSecurityContext(p: Principal): SecurityContext {
            return object : SecurityContext {
                override fun getUserPrincipal(): Principal {
                    return p
                }

                override fun isUserInRole(role: String): Boolean {
                    return false
                }
            }
        }
    }

    init {
        issueOperation = createIssueOperation(initSTSProperties(crypto))
    }
}
