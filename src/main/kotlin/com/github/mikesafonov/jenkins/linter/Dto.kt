package com.github.mikesafonov.jenkins.linter

/**
 * @author Mike Safonov
 */

data class LinterResponse(val code: Int, val message: String) {
    val success: Boolean
        get() = code == 200
}

data class JenkinsCrumb(val crumb: String, val crumbRequestField: String)

