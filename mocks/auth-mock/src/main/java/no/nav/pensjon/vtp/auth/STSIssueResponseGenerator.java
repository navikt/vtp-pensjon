package no.nav.pensjon.vtp.auth;

import static java.util.Collections.singletonList;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import org.apache.cxf.security.SecurityContext;
import org.apache.cxf.sts.StaticSTSProperties;
import org.apache.cxf.sts.operation.TokenIssueOperation;
import org.apache.cxf.sts.request.ReceivedToken;
import org.apache.cxf.sts.request.RequestRequirements;
import org.apache.cxf.sts.request.TokenRequirements;
import org.apache.cxf.sts.token.delegation.UsernameTokenDelegationHandler;
import org.apache.cxf.sts.token.provider.SAMLTokenProvider;
import org.apache.cxf.sts.token.provider.TokenProviderParameters;
import org.apache.cxf.ws.security.sts.provider.model.ObjectFactory;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseCollectionType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenType;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.principal.CustomTokenPrincipal;
import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.felles.CryptoConfigurationParameters;

@Component
public class STSIssueResponseGenerator {

    private static final String USERNAME = "goddagmannøkseskaft";
    private final CryptoConfigurationParameters cryptoConfiguration;
    private final TokenIssueOperation issueOperation;

    public STSIssueResponseGenerator(Crypto crypto, final CryptoConfigurationParameters cryptoConfiguration) {
        this.cryptoConfiguration = cryptoConfiguration;
        this.issueOperation = createIssueOperation(initSTSProperties(crypto));
    }


    static class PasswordCallbackHandler implements CallbackHandler {

        private final CryptoConfigurationParameters cryptoConfigurationParameters;

        public PasswordCallbackHandler(CryptoConfigurationParameters cryptoConfigurationParameters) {
            this.cryptoConfigurationParameters = cryptoConfigurationParameters;
        }

        @Override
        public void handle(Callback[] callbacks) {
            for (Callback callback : callbacks) {
                if (callback instanceof WSPasswordCallback) { // CXF
                    WSPasswordCallback pc = (WSPasswordCallback) callback;
                    if (cryptoConfigurationParameters.getKeyAndCertAlias().equals(pc.getIdentifier())) {
                        pc.setPassword(cryptoConfigurationParameters.getKeystorePassword());
                        break;
                    }
                }
            }
        }
    }

    private static TokenIssueOperation createIssueOperation(StaticSTSProperties staticSTSProperties) {
        TokenIssueOperation tokenIssueOperation = new TokenIssueOperation() {
            @Override
            protected TokenProviderParameters createTokenProviderParameters(RequestRequirements requestRequirements,
                    Principal principal,
                    Map<String, Object> messageContext) {
                TokenProviderParameters providerParameters = super.createTokenProviderParameters(requestRequirements, principal, messageContext);
                TokenRequirements tokenRequirements = providerParameters.getTokenRequirements();
                tokenRequirements.setOnBehalfOf(null);
                tokenRequirements.setActAs(null);
                return providerParameters;
            }
        };

        // Add Token Provider
        tokenIssueOperation.setTokenProviders(singletonList(new SAMLTokenProvider()));

        // Add TokenDelegationHandler for onBehalfOf
        tokenIssueOperation.setDelegationHandlers(singletonList(
                new UsernameTokenDelegationHandler() {
                    @Override
                    public boolean canHandleToken(ReceivedToken delegateTarget) {
                        return true;
                    }
                }
                ));

        tokenIssueOperation.setStsProperties(staticSTSProperties);

        return tokenIssueOperation;
    }

    private Map<String, Object> createMessageContext(Principal principal) {
        Map<String, Object> messageContext = new HashMap<>();
        messageContext.put(
            SecurityContext.class.getName(),
            createSecurityContext(principal));
        return messageContext;
    }

    public RequestSecurityTokenResponseCollectionType buildRequestSecurityTokenResponseCollectionType(RequestSecurityTokenCollectionType requestCollection) {
        Principal principal = new CustomTokenPrincipal(USERNAME);
        Map<String, Object> messageContext = createMessageContext(principal);
        return issueOperation.issue(requestCollection, principal, messageContext);
    }

    /** Issue a token as part of collection */
    public RequestSecurityTokenResponseCollectionType buildRequestSecurityTokenResponseCollectionType(RequestSecurityTokenType request) {
        Principal principal = new CustomTokenPrincipal(USERNAME);
        Map<String, Object> messageContext = createMessageContext(principal);
        return issueOperation.issue(request, principal, messageContext);
    }

    /** Issue a single token */
    public RequestSecurityTokenResponseType buildRequestSecurityTokenResponseType(RequestSecurityTokenType request) {
        Principal principal = new CustomTokenPrincipal(USERNAME);
        Map<String, Object> messageContext = createMessageContext(principal);
        return issueOperation.issueSingle(request, principal, messageContext);
    }

    /** Issue a single token */
    public RequestSecurityTokenResponseType buildRequestSecurityTokenResponseType(String tokenType){
        ObjectFactory of = new ObjectFactory();
        RequestSecurityTokenType request2 = of.createRequestSecurityTokenType();
        request2.getAny().add(of.createTokenType(tokenType));
        request2.getAny().add(of.createRequestType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue"));

        Principal principal = new CustomTokenPrincipal(USERNAME);
        Map<String, Object> messageContext = createMessageContext(principal);
        return issueOperation.issueSingle(request2, principal, messageContext);
    }

    private static SecurityContext createSecurityContext(final Principal p) {
        return new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return p;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }
        };
    }

    private StaticSTSProperties initSTSProperties(Crypto crypto) {
        // Add STSProperties object
        String alias = cryptoConfiguration.getKeyAndCertAlias();
        StaticSTSProperties stsProperties = new StaticSTSProperties();
        stsProperties.setEncryptionCrypto(crypto);
        stsProperties.setSignatureCrypto(crypto);
        stsProperties.setCallbackHandler(new PasswordCallbackHandler(cryptoConfiguration));
        stsProperties.setSignatureUsername(alias);
        stsProperties.setIssuer("VTP");
        return stsProperties;
    }
}
