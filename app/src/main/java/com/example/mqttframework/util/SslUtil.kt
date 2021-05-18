package com.example.mqttframework.util


import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMEncryptedKeyPair
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.FileReader
import java.security.KeyPair
import java.security.KeyStore
import java.security.Security
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory


object SSLUtil {
    @Throws(Exception::class)
    fun getSocketFactory(
        caCrtFile: String?,
        crtFile: String?, keyFile: String?, password: String
    ): SSLSocketFactory {
        Security.addProvider(BouncyCastleProvider())

        // load CA certificate
        var caCert: X509Certificate? = null
        val fis = FileInputStream(caCrtFile)
        var bis = BufferedInputStream(fis)
        val cf = CertificateFactory.getInstance("X.509")
        while (bis.available() > 0) {
            caCert = cf.generateCertificate(bis) as X509Certificate
            // System.out.println(caCert.toString());
        }

        // load client certificate
        bis = BufferedInputStream(FileInputStream(crtFile))
        var cert: X509Certificate? = null
        while (bis.available() > 0) {
            cert = cf.generateCertificate(bis) as X509Certificate
            // System.out.println(caCert.toString());
        }

        // load client private key
        val pemParser = PEMParser(FileReader(keyFile))
        val `object` = pemParser.readObject()
        val decProv = JcePEMDecryptorProviderBuilder()
            .build(password.toCharArray())
        val converter = JcaPEMKeyConverter()
        // .setProvider("BC");
        val key: KeyPair
        key = if (`object` is PEMEncryptedKeyPair) {
            println("Encrypted key - we will use provided password")
            converter.getKeyPair(
                `object`
                    .decryptKeyPair(decProv)
            )
        } else {
            println("Unencrypted key - no password needed")
            converter.getKeyPair(`object` as PEMKeyPair)
        }
        pemParser.close()

        // CA certificate is used to authenticate server
        val caKs = KeyStore.getInstance(KeyStore.getDefaultType())
        caKs.load(null, null)
        caKs.setCertificateEntry("ca-certificate", caCert)
        val tmf = TrustManagerFactory.getInstance("X509")
        tmf.init(caKs)

        // client key and certificates are sent to server so it can authenticate
        // us
        val ks = KeyStore.getInstance(KeyStore.getDefaultType())
        ks.load(null, null)
        ks.setCertificateEntry("certificate", cert)
        ks.setKeyEntry(
            "private-key",
            key.private,
            password.toCharArray(),
            arrayOf<Certificate?>(cert)
        )
        val kmf = KeyManagerFactory.getInstance(
            KeyManagerFactory
                .getDefaultAlgorithm()
        )
        kmf.init(ks, password.toCharArray())

        // finally, create SSL socket factory
        val context = SSLContext.getInstance("TLSv1.2")
        context.init(kmf.keyManagers, tmf.trustManagers, null)
        return context.socketFactory
    }
}
