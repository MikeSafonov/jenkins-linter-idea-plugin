package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.settings.JenkinsLinterCredentials
import com.intellij.credentialStore.Credentials

/**
 * @author Mike Safonov
 */
object JenkinsServerFactory {

    fun get(
        url: String,
        trustSelfSigned: Boolean = true,
        ignoreCertificate: Boolean = false,
        useCrumbIssuer: Boolean = false
    ): JenkinsServer {
        val credentials = JenkinsLinterCredentials.get()
        return JenkinsServerImpl(url, trustSelfSigned, ignoreCertificate, credentials, useCrumbIssuer)
    }

    fun get(
        url: String,
        trustSelfSigned: Boolean = true,
        ignoreCertificate: Boolean = false,
        credentials: Credentials?
    ): JenkinsServer {
        return JenkinsServerImpl(url, trustSelfSigned, ignoreCertificate, credentials)
    }
}
