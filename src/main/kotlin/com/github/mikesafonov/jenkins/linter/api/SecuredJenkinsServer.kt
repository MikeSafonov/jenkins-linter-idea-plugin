package com.github.mikesafonov.jenkins.linter.api

import com.intellij.credentialStore.Credentials
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntityBuilder

/**
 * @author Mike Safonov
 */
class SecuredJenkinsServer(
    url: String,
    trustSelfSigned: Boolean,
    credentials: Credentials,
    private val useCrumbIssuer: Boolean = false
) :
    BaseJenkinsServer(url, trustSelfSigned, credentials) {
    private val crumbIssuer = JenkinsCrumbIssuer(
        url,
        httpClient
    )

    override fun buildPost(fileContent: String): HttpPost {
        val postMethod = postRequest("/pipeline-model-converter/validate")

        if (useCrumbIssuer) {
            val crumb = crumbIssuer.get()
            postMethod.addHeader(crumb.crumbRequestField, crumb.crumb)
        }

        postMethod.entity = MultipartEntityBuilder.create()
            .addTextBody("jenkinsfile", fileContent)
            .build()
        return postMethod
    }
}
