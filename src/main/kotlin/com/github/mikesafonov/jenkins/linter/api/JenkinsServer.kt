package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.JenkinsResponse
import com.github.mikesafonov.jenkins.linter.LinterResponse
import java.io.Closeable

/**
 * @author Mike Safonov
 */
interface JenkinsServer : Closeable {

    fun checkConnection(): JenkinsResponse

    fun lint(content: String): LinterResponse
}
