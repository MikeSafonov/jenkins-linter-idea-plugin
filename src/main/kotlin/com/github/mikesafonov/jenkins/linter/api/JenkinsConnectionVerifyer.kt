package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.JenkinsLinterException
import com.intellij.credentialStore.Credentials

/**
 * @author Mike Safonov
 */
class JenkinsConnectionVerifyer {

    fun verify(
        host: String,
        trustSelfSigned: Boolean = false,
        ignoreCertificate: Boolean = false,
        credentials: Credentials?
    ) {
        val jenkinsServer = JenkinsServerFactory.get(host, trustSelfSigned, ignoreCertificate, credentials)
        jenkinsServer.use {
            val response = jenkinsServer.checkConnection()
            if (!response.success) {
                throw JenkinsLinterException("Request return status code ${response.code}", response.code)
            }
        }
    }
}
