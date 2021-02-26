package com.github.mikesafonov.jenkins.linter.api

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntityBuilder

/**
 * @author Mike Safonov
 */
class SimpleJenkinsServer(url: String, trustSelfSigned: Boolean = true) : BaseJenkinsServer(url, trustSelfSigned) {

    override fun buildPost(fileContent: String): HttpPost {
        val postMethod = postRequest("/pipeline-model-converter/validate")
        postMethod.entity = MultipartEntityBuilder.create()
            .addTextBody("jenkinsfile", fileContent)
            .build()
        return postMethod
    }
}
