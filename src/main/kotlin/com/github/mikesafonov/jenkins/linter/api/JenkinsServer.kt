package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.JenkinsResponse
import com.github.mikesafonov.jenkins.linter.LinterResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.apache.http.util.EntityUtils
import java.io.Closeable
import java.nio.charset.StandardCharsets

/**
 * @author Mike Safonov
 */
class JenkinsServer(private val url: String, ignoreSelfSigned: Boolean = true) : Closeable {
    private val httpClient: CloseableHttpClient = if (ignoreSelfSigned) {
        val builder = SSLContextBuilder()
        builder.loadTrustMaterial(TrustSelfSignedStrategy())
        HttpClients.custom().setSSLSocketFactory(
            SSLConnectionSocketFactory(builder.build())
        ).build()
    } else {
        HttpClients.createDefault()
    }

    fun checkConnection(): JenkinsResponse {
        return httpClient.execute(getRequest()).use {
            val code = it.statusLine.statusCode
            JenkinsResponse(code)
        }
    }

    fun lint(content: String): LinterResponse {
        val postMethod = buildPost(content)
        return httpClient.execute(postMethod).use {
            val response = EntityUtils.toString(it.entity, StandardCharsets.UTF_8)
            LinterResponse(it.statusLine.statusCode, response)
        }
    }

    override fun close() {
        httpClient.close()
    }

    private fun getRequest(): HttpGet {
        return HttpGet(url)
    }

    private fun postRequest(path: String): HttpPost {
        val req = URIBuilder(url).setPath(path).build().normalize()
        return HttpPost(req)
    }

    private fun buildPost(fileContent: String): HttpPost {
        val postMethod = postRequest("/pipeline-model-converter/validate")
        postMethod.entity = MultipartEntityBuilder.create()
            .addTextBody("jenkinsfile", fileContent)
            .build()
        return postMethod
    }
}
