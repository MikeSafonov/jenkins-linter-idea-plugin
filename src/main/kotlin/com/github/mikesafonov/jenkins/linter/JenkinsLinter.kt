package com.github.mikesafonov.jenkins.linter

import com.intellij.util.io.HttpRequests

/**
 * @author Mike Safonov
 */
class JenkinsLinter(val jenkinsLinterSettings: JenkinsLinterSettings) {

    fun lint(fileContent: String) {

        val requestString = "${jenkinsLinterSettings.jenkinsUrl}?jenkinsfile=$fileContent"

        println(HttpRequests.post(requestString, null).readString())
    }
}