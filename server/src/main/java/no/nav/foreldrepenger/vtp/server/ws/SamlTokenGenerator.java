package no.nav.foreldrepenger.vtp.server.ws;

import no.nav.foreldrepenger.vtp.felles.KeyStoreTool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SamlTokenGenerator {
    private static final DateTimeFormatter format = DateTimeFormatter.ISO_INSTANT;

    public String issueToken(String username) throws Exception {
        try {
            String id = "SAML-" + UUID.randomUUID().toString();
            String samlToken = getUnsignedSaml(id, username);
            DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
            docFac.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFac.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(samlToken)));
            doc.getDocumentElement().normalize();

            Node assertionNode = doc.getFirstChild();
            Node nextSibling = assertionNode.getFirstChild().getNextSibling();

            // SignedInfo
            XMLSignatureFactory signFac = XMLSignatureFactory.getInstance("DOM");
            List<Transform> tList = new ArrayList<>();
            tList.add(signFac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
            tList.add(signFac.newTransform(CanonicalizationMethod.EXCLUSIVE, (TransformParameterSpec) null));
            Reference ref = signFac.newReference("#" + id, signFac.newDigestMethod(DigestMethod.SHA1, null), tList, null, null);
            SignedInfo si = signFac.newSignedInfo(signFac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null),
                    signFac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(ref));

            X509Certificate cert = KeyStoreTool.getDefaultCredential().getEntityCertificate();
            PrivateKey privateKey = KeyStoreTool.getDefaultCredential().getPrivateKey();
            if(privateKey == null) {
                throw new IllegalArgumentException("Failed to find PrivateKey in keystore");
            }

            KeyInfoFactory kiFac = signFac.getKeyInfoFactory();
            X509IssuerSerial x509IssuerSerial = kiFac.newX509IssuerSerial(cert.getIssuerDN().getName(), cert.getSerialNumber());
            List<Object> dList = new ArrayList<>();
            dList.add(cert);
            dList.add(x509IssuerSerial);
            X509Data x509data = kiFac.newX509Data(dList);
            List<XMLStructure> kiList = new ArrayList<>();
            kiList.add(x509data);
            KeyInfo ki = kiFac.newKeyInfo(kiList);

            // Signature
            XMLSignature signature = signFac.newXMLSignature(si, ki);
            DOMSignContext context = new DOMSignContext(privateKey, assertionNode, nextSibling);
            context.setIdAttributeNS((Element) assertionNode, null, "ID");
            signature.sign(context);

            // Transform to XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);

            return result.getWriter().toString();
        }
        catch(Exception e) {
            throw new Exception("Error: " + e.getMessage(), e);
        }
    }

    private String getUnsignedSaml(String id, String nameID) {
        ZonedDateTime currentDate =  ZonedDateTime.now();
        return "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" ID=\"" + id + "\"" +
                " IssueInstant=\"" + currentDate.format(format) + "\" Version=\"2.0\">" +
                "<saml2:Issuer>VTP</saml2:Issuer>" +
                "<saml2:Subject><saml2:NameID Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\">" + nameID +
                "</saml2:NameID><saml2:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\">" +
                "<saml2:SubjectConfirmationData NotBefore=\"" + currentDate.format(format) + "\"/></saml2:SubjectConfirmation></saml2:Subject>" +
                "<saml2:Conditions NotBefore=\"" + currentDate.format(format) + "\"/><saml2:AttributeStatement>" +
                "<saml2:Attribute Name=\"identType\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"><saml2:AttributeValue>InternBruker</saml2:AttributeValue></saml2:Attribute>" +
                "</saml2:AttributeStatement></saml2:Assertion>";
    }
}
