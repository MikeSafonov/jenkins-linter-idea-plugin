package com.github.mikesafonov.jenkins.linter.api

import com.intellij.credentialStore.Credentials
import com.intellij.util.net.IdeHttpClientHelpers
import com.intellij.util.net.ssl.CertificateManager
import org.apache.commons.codec.binary.Base64.encodeBase64
import org.apache.http.HttpHeaders
import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.apache.http.ssl.SSLContextBuilder
import java.nio.charset.StandardCharsets

/**
 * @author Mike Safonov
 */
object HttpClientFactory {

    fun get(url: String, trustSelfSigned: Boolean, credentials: Credentials?): CloseableHttpClient {
        val clientBuilder = HttpClients.custom()

        // proxy support
        val provider = BasicCredentialsProvider()
        IdeHttpClientHelpers.ApacheHttpClient4.setProxyCredentialsForUrlIfEnabled(provider, url)
        val requestConfig = RequestConfig.custom()
        IdeHttpClientHelpers.ApacheHttpClient4.setProxyForUrlIfEnabled(requestConfig, url)

        // ssl support
        clientBuilder
            .setSSLContext(CertificateManager.getInstance().sslContext)
            .setDefaultRequestConfig(requestConfig.build())
            .setDefaultCredentialsProvider(provider)
        if (trustSelfSigned) {
            val builder = SSLContextBuilder()
            builder.loadTrustMaterial(TrustSelfSignedStrategy())
            clientBuilder.setSSLSocketFactory(
                SSLConnectionSocketFactory(builder.build())
            )
        }
        if (credentials != null) {
            val auth = "${credentials.userName!!}:${credentials.getPasswordAsString()}"
            val encodedAuth: ByteArray = encodeBase64(
                auth.toByteArray(StandardCharsets.ISO_8859_1)
            )
            val authHeader = "Basic ${String(encodedAuth)}"
            clientBuilder.setDefaultHeaders(
                listOf(BasicHeader(HttpHeaders.AUTHORIZATION, authHeader))
            )
        }
        return clientBuilder.build()
    }
}
