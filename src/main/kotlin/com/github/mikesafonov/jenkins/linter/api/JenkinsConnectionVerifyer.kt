package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.JenkinsLinterException

/**
 * @author Mike Safonov
 */
class JenkinsConnectionVerifyer {

    fun verify(host: String, trustSelfSigned: Boolean = false) {
        val response = JenkinsServer(host, trustSelfSigned).checkConnection()
        if (!response.success) {
            throw JenkinsLinterException("Request return status code ${response.code}")
        }
    }
}
