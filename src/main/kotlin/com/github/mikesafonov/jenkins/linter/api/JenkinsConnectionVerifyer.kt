package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.JenkinsLinterException

/**
 * @author Mike Safonov
 */
class JenkinsConnectionVerifyer {

    fun verify(host: String) {
        val response = JenkinsServer(host).checkConnection()
        if (!response.success) {
            throw JenkinsLinterException("Request return status code ${response.code}")
        }
    }
}
