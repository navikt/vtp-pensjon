package no.nav.pensjon.vtp.auth

import org.opensaml.security.x509.X509Credential
import org.springframework.stereotype.Component
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import java.io.StringWriter
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.crypto.XMLStructure
import javax.xml.crypto.dsig.*
import javax.xml.crypto.dsig.dom.DOMSignContext
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec
import javax.xml.crypto.dsig.spec.TransformParameterSpec
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.Throws

@Component
class SamlTokenGenerator(private val x509Credential: X509Credential) {
    @Throws(Exception::class)
    fun issueToken(username: String): String {
        return try {
            val id = "SAML-" + UUID.randomUUID().toString()
            val samlToken = getUnsignedSaml(id, username)
            val docFac = DocumentBuilderFactory.newInstance()
            docFac.isNamespaceAware = true
            val docBuilder = docFac.newDocumentBuilder()
            val doc = docBuilder.parse(InputSource(StringReader(samlToken)))
            doc.documentElement.normalize()
            val assertionNode = doc.firstChild
            val nextSibling = assertionNode.firstChild.nextSibling

            // SignedInfo
            val signFac = XMLSignatureFactory.getInstance("DOM")
            val tList: MutableList<Transform> = ArrayList()
            tList.add(signFac.newTransform(Transform.ENVELOPED, null as TransformParameterSpec?))
            tList.add(signFac.newTransform(CanonicalizationMethod.EXCLUSIVE, null as TransformParameterSpec?))
            val ref = signFac.newReference("#$id", signFac.newDigestMethod(DigestMethod.SHA1, null), tList, null, null)
            val si = signFac.newSignedInfo(
                signFac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, null as C14NMethodParameterSpec?),
                signFac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), listOf(ref)
            )
            val cert = x509Credential.entityCertificate
            val privateKey =
                x509Credential.privateKey ?: throw IllegalArgumentException("Failed to find PrivateKey in keystore")
            val kiFac = signFac.keyInfoFactory
            val x509IssuerSerial = kiFac.newX509IssuerSerial(cert.issuerDN.name, cert.serialNumber)
            val dList: MutableList<Any?> = ArrayList()
            dList.add(cert)
            dList.add(x509IssuerSerial)
            val x509data = kiFac.newX509Data(dList)
            val kiList: MutableList<XMLStructure> = ArrayList()
            kiList.add(x509data)
            val ki = kiFac.newKeyInfo(kiList)

            // Signature
            val signature = signFac.newXMLSignature(si, ki)
            val context = DOMSignContext(privateKey, assertionNode, nextSibling)
            context.setIdAttributeNS(assertionNode as Element, null, "ID")
            signature.sign(context)

            // Transform to XML
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty("omit-xml-declaration", "yes")
            val source = DOMSource(doc)
            val result = StreamResult(StringWriter())
            transformer.transform(source, result)
            result.writer.toString()
        } catch (e: Exception) {
            throw Exception("Error: " + e.message, e)
        }
    }

    private fun getUnsignedSaml(id: String, nameID: String): String {
        val currentDate = ZonedDateTime.now()
        return "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" ID=\"" + id + "\"" +
            " IssueInstant=\"" + currentDate.format(format) + "\" Version=\"2.0\">" +
            "<saml2:Issuer>VTP</saml2:Issuer>" +
            "<saml2:Subject><saml2:NameID Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\">" + nameID +
            "</saml2:NameID><saml2:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\">" +
            "<saml2:SubjectConfirmationData NotBefore=\"" + currentDate.format(format) + "\"/></saml2:SubjectConfirmation></saml2:Subject>" +
            "<saml2:Conditions NotBefore=\"" + currentDate.format(format) + "\"/><saml2:AttributeStatement>" +
            "<saml2:Attribute Name=\"identType\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"><saml2:AttributeValue>InternBruker</saml2:AttributeValue></saml2:Attribute>" +
            "</saml2:AttributeStatement></saml2:Assertion>"
    }

    companion object {
        private val format = DateTimeFormatter.ISO_INSTANT
    }
}
