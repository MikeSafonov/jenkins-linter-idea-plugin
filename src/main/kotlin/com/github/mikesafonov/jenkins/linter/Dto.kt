package com.github.mikesafonov.jenkins.linter

/**
 * @author Mike Safonov
 */

data class LinterResponse(val code: Int, val message: String) {
    val success: Boolean
        get() = code == 200
}

