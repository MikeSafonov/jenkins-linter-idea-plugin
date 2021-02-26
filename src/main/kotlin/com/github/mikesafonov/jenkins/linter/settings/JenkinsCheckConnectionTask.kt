package com.github.mikesafonov.jenkins.linter.settings

import com.github.mikesafonov.jenkins.linter.JenkinsLinterException
import com.github.mikesafonov.jenkins.linter.api.JenkinsConnectionVerifyer
import com.intellij.credentialStore.Credentials
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task

/**
 * @author Mike Safonov
 */
class JenkinsCheckConnectionTask(
    private val jenkinsUrl: String,
    private val trustSelfSigned: Boolean,
    private val credentials: Credentials?
) :
    Task.Modal(null, "Test Connection to Jenkins", false) {

    var success: Boolean = false

    override fun run(indicator: ProgressIndicator) {
        indicator.text = "Connecting to $jenkinsUrl ... "
        indicator.isIndeterminate = true

        try {
            JenkinsConnectionVerifyer().verify(jenkinsUrl, trustSelfSigned, credentials)
            success = true
        } catch (e: JenkinsLinterException) {
            Logger.getInstance(JenkinsCheckConnectionTask::class.java).debug(e)
        }
    }
}
