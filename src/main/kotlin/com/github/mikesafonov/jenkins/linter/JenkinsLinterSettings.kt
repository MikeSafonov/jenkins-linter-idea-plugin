package com.github.mikesafonov.jenkins.linter


/**
 * @author Mike Safonov
 */

data class JenkinsLinterSettings(val jenkinsUrl: String, val username: String, val password: String, val ignoreSsl: Boolean = false)