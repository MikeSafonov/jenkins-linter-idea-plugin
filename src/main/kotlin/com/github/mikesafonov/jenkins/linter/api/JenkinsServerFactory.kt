package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.settings.JenkinsLinterCredentials
import com.intellij.credentialStore.Credentials

/**
 * @author Mike Safonov
 */
object JenkinsServerFactory {

    fun get(url: String, trustSelfSigned: Boolean = true): JenkinsServer {
        return get(url, trustSelfSigned, JenkinsLinterCredentials.get())
    }

    fun get(url: String, trustSelfSigned: Boolean = true, credentials: Credentials?): JenkinsServer {
        return if (credentials == null) {
            simple(url, trustSelfSigned)
        } else {
            secured(url, trustSelfSigned, credentials)
        }
    }

    private fun simple(url: String, trustSelfSigned: Boolean): JenkinsServer {
        return SimpleJenkinsServer(url, trustSelfSigned)
    }

    private fun secured(url: String, trustSelfSigned: Boolean, credentials: Credentials): JenkinsServer {
        return SecuredJenkinsServer(url, trustSelfSigned, credentials)
    }
}
