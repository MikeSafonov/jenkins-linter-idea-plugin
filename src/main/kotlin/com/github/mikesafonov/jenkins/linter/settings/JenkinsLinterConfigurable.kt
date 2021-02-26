package com.github.mikesafonov.jenkins.linter.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.util.Comparing
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * @author Mike Safonov
 */
class JenkinsLinterConfigurable : SearchableConfigurable {
    private val component: JenkinsLinterComponent = JenkinsLinterComponent()

    override fun createComponent(): JComponent {
        reset()
        return component.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = getSettings()
        val modified = settings.jenkinsUrl != component.getJenkinsUrl() ||
            settings.trustSelfSigned != component.getTrustSelfSigned()

        if (modified) {
            return true
        }
        val credentials = JenkinsLinterCredentials.get()
        return if (credentials == null) {
            true
        } else {
            !Comparing.equal(credentials.userName, component.getUsername()) ||
                !Comparing.equal(credentials.getPasswordAsString(), component.getPassword())
        }
    }

    override fun apply() {
        val settings = getSettings()
        settings.jenkinsUrl = component.getJenkinsUrl()
        settings.trustSelfSigned = component.getTrustSelfSigned()

        val credentials = component.getCredentials()
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
        component.setJenkinsUrl(settings.jenkinsUrl)
        component.setTrustSelfSigned(settings.trustSelfSigned)
        component.setCredentials(JenkinsLinterCredentials.get())
    }

    private fun getSettings(): JenkinsLinterState {
        return JenkinsLinterState.getInstance()
    }
}
