package com.github.mikesafonov.jenkins.linter

import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.nio.charset.StandardCharsets


/**
 * @author Mike Safonov
 */
class JenkinsLinter {

    private val url: String
    private val httpClient: HttpClient
    private val useCrumb: Boolean
    private val crumbIssuer: JenkinsCrumbIssuer?
    private val context: HttpClientContext?

    constructor(url: String, useCrumb: Boolean) {
        this.url = url
        httpClient = HttpClients.createDefault()
        this.useCrumb = useCrumb
        context = null
        crumbIssuer = if (useCrumb) {
            JenkinsCrumbIssuer(url, httpClient, context)
        } else {
            null
        }
    }

    constructor(url: String, username: String, password: String, useCrumb: Boolean) {
        this.url = url
        this.useCrumb = useCrumb
        httpClient = HttpClients.createDefault()
        val provider = BasicCredentialsProvider()
        provider.setCredentials(
                AuthScope.ANY,
                UsernamePasswordCredentials(username, password))
        context = HttpClientContext.create()
        context.credentialsProvider = provider

        crumbIssuer = if (useCrumb) {
            JenkinsCrumbIssuer(url, httpClient, context)
        } else {
            null
        }
    }

    /**
     * @param fileContent linting file content
     * @return lint response
     */
    fun lint(fileContent: String): LinterResponse {

        val postMethod = buildPost(fileContent)
        val response = if (context != null) {
            httpClient.execute(postMethod, context)
        } else {
            httpClient.execute(postMethod)
        }
        val content = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)

        return LinterResponse(response.statusLine.statusCode, content)
    }

    private fun buildPost(fileContent: String): HttpPost {
        val postMethod = HttpPost("${url}/pipeline-model-converter/validate")
        if (crumbIssuer != null) {
            val crumb = crumbIssuer.get()
            postMethod.addHeader(crumb.crumbRequestField, crumb.crumb)
        }
        postMethod.entity = MultipartEntityBuilder.create()
                .addTextBody("jenkinsfile", fileContent)
                .build()
        return postMethod
    }
}
