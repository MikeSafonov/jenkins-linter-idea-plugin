package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.JenkinsLinterException
import com.github.mikesafonov.jenkins.linter.JenkinsResponse
import com.github.mikesafonov.jenkins.linter.LinterResponse
import com.intellij.credentialStore.Credentials
import com.intellij.openapi.diagnostic.Logger
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import java.nio.charset.StandardCharsets

/**
 * @author Mike Safonov
 */
abstract class BaseJenkinsServer(
    private val url: String,
    trustSelfSigned: Boolean = true,
    credentials: Credentials? = null
) : JenkinsServer {
    protected val httpClient: CloseableHttpClient = HttpClientFactory.get(trustSelfSigned, credentials)

    override fun checkConnection(): JenkinsResponse {
        return httpClient.execute(getRequest()).use {
            val code = it.statusLine.statusCode
            JenkinsResponse(code)
        }
    }

    override fun close() {
        httpClient.close()
    }

    override fun lint(content: String): LinterResponse {
        return try {
            val postMethod = buildPost(content)
            httpClient.execute(postMethod).use {
                val response = EntityUtils.toString(it.entity, StandardCharsets.UTF_8)
                LinterResponse(it.statusLine.statusCode, response)
            }
        } catch (ex: JenkinsLinterException) {
            Logger.getInstance(BaseJenkinsServer::class.java).debug(ex)
            LinterResponse(ex.statusCode, ex.message ?: ex.cause?.message ?: "")
        }
    }

    protected abstract fun buildPost(fileContent: String): HttpPost

    protected fun postRequest(path: String): HttpPost {
        val req = URIBuilder(url).setPath(path).build().normalize()
        return HttpPost(req)
    }

    private fun getRequest(): HttpGet {
        return HttpGet(url)
    }
}
