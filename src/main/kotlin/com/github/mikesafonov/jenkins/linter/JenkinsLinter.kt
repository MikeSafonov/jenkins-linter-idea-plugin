package com.github.mikesafonov.jenkins.linter

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.NameValuePair
import org.apache.commons.httpclient.methods.PostMethod

/**
 * @author Mike Safonov
 */
class JenkinsLinter {

    private val jenkinsLinterSettings = JenkinsLinterSettings.getInstance()

    fun lint(fileContent: String): LinterResponse {

        val postMethod = buildPost(fileContent)
        val methodResponseCode = HttpClient().executeMethod(postMethod)

        return LinterResponse(methodResponseCode, postMethod.responseBodyAsString)
    }

    private fun buildPost(fileContent: String): PostMethod {
        val data = arrayOf(NameValuePair("jenkinsfile", fileContent))
        val postMethod = PostMethod("${jenkinsLinterSettings.jenkinsUrl}/pipeline-model-converter/validate")
        postMethod.setRequestBody(data)
        return postMethod
    }
}