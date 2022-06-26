package com.github.mikesafonov.jenkins.linter.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.util.Comparing
import org.jetbrains.annotations.Nls
import java.util.Objects
import javax.swing.JComponent

/**
 * @author Mike Safonov
 */
class JenkinsLinterConfigurable : SearchableConfigurable {
    private val component: JenkinsLinterComponent = JenkinsLinterComponent()

    override fun createComponent(): JComponent {
        reset()
        return component.panel
    }

    override fun isModified(): Boolean {
        val settings = getSettings()
        val modified = settings.jenkinsUrl != component.jenkinsUrl ||
            settings.trustSelfSigned != component.trustSelfSigned ||
            settings.ignoreCertificate != component.ignoreCertificate ||
            settings.useCrumbIssuer != component.useCrumbIssuer

        if (modified) {
            return true
        }
        val credentials = JenkinsLinterCredentials.get()
        return if (credentials == null) {
            true
        } else {
            !Objects.equals(credentials.userName, component.username) ||
                !Comparing.equal(credentials.getPasswordAsString(), component.password)
        }
    }

    override fun apply() {
        val settings = getSettings()
        val jenkinsUrl = component.jenkinsUrl
        settings.jenkinsUrl = if (jenkinsUrl.endsWith("/")) {
            jenkinsUrl.substring(0, jenkinsUrl.length - 1)
        } else jenkinsUrl

        settings.trustSelfSigned = component.trustSelfSigned
        settings.ignoreCertificate = component.ignoreCertificate
        settings.useCrumbIssuer = component.useCrumbIssuer

        val credentials = component.credentials
        if (credentials != null) {
            JenkinsLinterCredentials.store(credentials)
        }
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String {
        return "Jenkins Linter"
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getId(): String {
        return "Jenkins Linter"
    }

    override fun reset() {
        val settings = getSettings()
        component.jenkinsUrl = settings.jenkinsUrl
        component.trustSelfSigned = settings.trustSelfSigned
        component.ignoreCertificate = settings.ignoreCertificate
        component.useCrumbIssuer = settings.useCrumbIssuer
        component.credentials = JenkinsLinterCredentials.get()
    }

    private fun getSettings(): JenkinsLinterState {
        return JenkinsLinterState.getInstance()
    }
}
