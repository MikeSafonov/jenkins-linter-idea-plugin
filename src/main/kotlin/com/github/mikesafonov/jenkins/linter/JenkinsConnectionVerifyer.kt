package com.github.mikesafonov.jenkins.linter

import com.intellij.openapi.diagnostic.Logger
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

/**
 * @author Mike Safonov
 */
class JenkinsConnectionVerifyer {

    fun verify(host: String): Boolean {
        return try {
            val httpClient = HttpClients.createDefault()
            val response = httpClient.execute(HttpGet(host))
            response.statusLine.statusCode == 200
        } catch (e: Exception) {
            Logger.getInstance(JenkinsConnectionVerifyer::class.java).debug(e)
            false
        }
    }
}
