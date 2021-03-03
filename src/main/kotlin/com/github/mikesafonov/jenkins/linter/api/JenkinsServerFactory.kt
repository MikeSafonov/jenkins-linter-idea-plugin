package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.settings.JenkinsLinterCredentials
import com.intellij.credentialStore.Credentials

/**
 * @author Mike Safonov
 */
object JenkinsServerFactory {

    fun get(url: String, trustSelfSigned: Boolean = true, useCrumbIssuer: Boolean = false): JenkinsServer {
        val credentials = JenkinsLinterCredentials.get()
        return JenkinsServerImpl(url, trustSelfSigned, credentials, useCrumbIssuer)
    }

    fun get(url: String, trustSelfSigned: Boolean = true, credentials: Credentials?): JenkinsServer {
        return JenkinsServerImpl(url, trustSelfSigned, credentials)
    }
}
