package no.nav.pensjon.vtp.application

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.cert.X509v1CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import java.io.*
import java.math.BigInteger.valueOf
import java.nio.file.Files
import java.security.*
import java.security.KeyStore.getInstance
import java.security.Security.addProvider
import java.security.cert.Certificate
import java.util.*

/**
 * Dette brukes bare for å generere et nytt nøkkel-par.
 * 1. Generer nøkkel-par ved å kjøre main-metoden
 * 2. Eksporter sertifikat etter generering:
 * keytool -export -keystore ~/.modig/keystore.jks -alias localhost-ssl -file ~/.modig/ vtp-pensjon.cer
 * 3. Importer sertifikaten inn i PEN lokale truststores som ligger i pesys/local/pen-secrets:
 * keytool -import -alias localhost -file ~/.modig/vtp-pensjon.cer -storetype JKS -keystore ~/pesys/local/pen-secrets/truststore/truststore.jts
 * 4. Importer sertifikaten inn i POPP lokale truststores som ligger i pesys/local/popp-secrets:
 * keytool -import -alias localhost -file ~/.modig/vtp-pensjon.cer -storetype JKS -keystore ~/pesys/local/popp-secrets/truststore/truststore.jts
 * 5. Lag en Pull-Request til pesys-master med oppdaterte lokale truststores
 * 6. Kopier og erstatt vtp truststore og keystore i keystore.jks og truststore med nye genererte
 * 7. Lag Pull-Request til vtp-master med oppdaterte truststore/keystore
 */
fun main(args: Array<String>) {
    val keystoreFile = File(System.getProperty("user.home", ".") + "/.modig/keystore.jks")
    val keystorePassword = "devillokeystore1234"
    val truststoreFile = File(System.getProperty("user.home", ".") + "/.modig/truststore.jks")
    val truststorePassword = "changeit"
    val keyAndCertAlias = "localhost-ssl"

    println("----------- GENERATING A NEW KEY PAIR -----------")
    Files.deleteIfExists(keystoreFile.toPath())
    Files.deleteIfExists(truststoreFile.toPath())
    keystoreFile.parentFile.mkdirs()
    truststoreFile.parentFile.mkdirs()

    createKeystoreAndTrustStore(keyAndCertAlias, keystoreFile, keystorePassword.toCharArray(), "JKS", truststoreFile, truststorePassword.toCharArray())
    println("-------------------- DONE -----------------------")
}

fun createKeystoreAndTrustStore(keyAndCertAlias: String, keystoreFile: File, keystorePassword: CharArray, outputFormat: String, truststorePath: File, truststorePassword: CharArray) {
    println("Generating keystore $keystoreFile with certificate for $keyAndCertAlias")
    val selfCert = generateKeystore(keystoreFile, keystorePassword, outputFormat, keyAndCertAlias)
    createTruststore(keyAndCertAlias, outputFormat, truststorePath, truststorePassword, selfCert)
}

private fun createTruststore(keyAndCertAlias: String, outputFormat: String, truststoreFile: File, truststorePassword: CharArray, selfCert: Certificate) {
    // Create the truststore, add the certificate, save to file
    println("Adding certificate to the truststore $truststoreFile")
    getInstance(outputFormat).apply {
        load(null, truststorePassword)
        setCertificateEntry(keyAndCertAlias, selfCert)
        writeKeystoreToFile(this, truststoreFile, truststorePassword)
    }
}

fun generateKeystore(keystoreFile: File, keystorePassword: CharArray, outputFormat: String?, keyAndCertAlias: String?): Certificate {
    addProvider(BouncyCastleProvider())

    // Generate public/private keypair and self-signed certificate
    val keyPair = KeyPairGenerator
            .getInstance("RSA").apply {
                initialize(2048)
            }.generateKeyPair()
    val selfCert = createMasterCert(keyPair.public, keyPair.private)

    // Create the keystore, add the two entries
    // (we just use the same private key twice, for simplicity...),
    // save to file
    val outChain = arrayOf(selfCert)
    getInstance(outputFormat).apply {
        load(null, keystorePassword)
        setKeyEntry(keyAndCertAlias, keyPair.private, keystorePassword, outChain)
        setKeyEntry("app-key", keyPair.private, keystorePassword, outChain)
        writeKeystoreToFile(this, keystoreFile, keystorePassword)
    }
    return selfCert
}

private fun writeKeystoreToFile(keyStore: KeyStore, file: File, password: CharArray) {
    file.outputStream().use { keyStore.store(it, password) }
}

private fun createMasterCert(pubKey: PublicKey, privKey: PrivateKey): Certificate {
    val issuer = "CN=vtp-pensjon"
    val subject = "CN=vtp-pensjon"

    // Valid from January 1st 1970 until ten years from now
    val validFrom = Date(0)
    val validUntil = Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 3650)

    val builder: X509v1CertificateBuilder = JcaX509v1CertificateBuilder(
            X500Name(issuer),
            valueOf(1),
            validFrom,
            validUntil,
            X500Name(subject),
            pubKey
    )

    return JcaX509CertificateConverter()
            .setProvider("BC")
            .getCertificate(builder.build(
                    JcaContentSignerBuilder("SHA1WithRSA")
                            .setProvider("BC")
                            .build(privKey)
            ))
            .apply {
                checkValidity(Date())
                verify(pubKey)
            }
}
