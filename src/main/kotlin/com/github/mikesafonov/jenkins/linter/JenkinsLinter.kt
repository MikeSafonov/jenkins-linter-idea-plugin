package com.github.mikesafonov.jenkins.linter

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.NameValuePair
import org.apache.commons.httpclient.methods.PostMethod

/**
 * @author Mike Safonov
 */
class JenkinsLinter {

    private val httpClient : HttpClient = HttpClient()

    /**
     * @param fileContent linting file content
     * @return lint response
     */
    fun lint(fileContent: String): LinterResponse {

        val postMethod = buildPost(fileContent)
        val methodResponseCode = httpClient.executeMethod(postMethod)

        return LinterResponse(methodResponseCode, postMethod.responseBodyAsString)
    }

    private fun buildPost(fileContent: String): PostMethod {
        val jenkinsLinterSettings = JenkinsLinterSettings.getInstance()
        val data = arrayOf(NameValuePair("jenkinsfile", fileContent))
        val postMethod = PostMethod("${jenkinsLinterSettings.jenkinsUrl}/pipeline-model-converter/validate")
        postMethod.setRequestBody(data)
        return postMethod
    }
}
