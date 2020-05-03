package com.github.mikesafonov.jenkins.linter

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe

/**
 * Linter credentials
 *
 * @author Mike Safonov
 */
object JenkinsLinterCredentials {

    fun get(): Credentials? {
        val attributes = credentialAttributes()
        return PasswordSafe.instance.get(attributes)
    }

    fun store(username: String, password: CharArray) {
        val attributes = credentialAttributes()
        val credentials = Credentials(username, password)
        PasswordSafe.instance.set(attributes, credentials)
    }

    private fun credentialAttributes() = CredentialAttributes(generateServiceName("Jenkins linter",
            "jenkins instance"))
}
