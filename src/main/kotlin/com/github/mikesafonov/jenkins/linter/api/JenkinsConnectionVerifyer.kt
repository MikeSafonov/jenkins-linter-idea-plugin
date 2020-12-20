package com.github.mikesafonov.jenkins.linter.api

/**
 * @author Mike Safonov
 */
class JenkinsConnectionVerifyer {

    fun verify(host: String) {
        val response = JenkinsServer(host).checkConnection()
        if (!response.success) {
            throw RuntimeException("Request return status code ${response.code}")
        }
    }
}
