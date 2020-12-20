package com.github.mikesafonov.jenkins.linter.settings

import com.intellij.openapi.options.SearchableConfigurable
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
        return settings.jenkinsUrl != component.getJenkinsUrl()
    }

    override fun apply() {
        val settings = getSettings()
        settings.jenkinsUrl = component.getJenkinsUrl()
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
    }

    private fun getSettings() : JenkinsLinterState {
        return JenkinsLinterState.getInstance()
    }
}
