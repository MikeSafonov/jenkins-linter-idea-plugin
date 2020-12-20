package com.github.mikesafonov.jenkins.linter.api

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

/**
 * @author Mike Safonov
 */
class JenkinsConnectionVerifyer {

    fun verify(host: String) {
        val httpClient = HttpClients.createDefault()
        val response = httpClient.execute(HttpGet(host))
        val code = response.statusLine.statusCode
        if (code != 200) {
            throw RuntimeException("Request return status code $code")
        }
    }
}
