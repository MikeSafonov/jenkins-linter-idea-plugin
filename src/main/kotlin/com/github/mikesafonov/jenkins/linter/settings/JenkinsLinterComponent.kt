package com.github.mikesafonov.jenkins.linter.settings

import com.intellij.credentialStore.Credentials
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JPasswordField
import javax.swing.JTextField

/**
 * @author Mike Safonov
 */
class JenkinsLinterComponent {
    private var panel: JPanel
    private var jenkinsUrlTextField: JTextField = JTextField()
    private var trustSelfSignedCheckbox: JCheckBox = JCheckBox()
    private var verifyButton = JButton("Check connection")
    private var usernameTextField: JTextField = JTextField()
    private var passwordTextField: JPasswordField = JPasswordField()
    private var useCrumbIssuerCheckbox: JCheckBox = JCheckBox()

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Jenkins url (protocol://hostname:port):"), jenkinsUrlTextField, 1, false)
            .addLabeledComponent(JBLabel("Trust self-signed:"), trustSelfSignedCheckbox, 1, false)
            .addLabeledComponent(JBLabel("Username:"), usernameTextField, 1, false)
            .addLabeledComponent(JBLabel("Password/Token:"), passwordTextField, 1, false)
            .addLabeledComponent(JBLabel("Use crumb issuer:"), useCrumbIssuerCheckbox, 1, false)
            .addComponent(verifyButton, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        verifyButton.addActionListener {
            checkConnection()
        }
    }

    fun getJenkinsUrl(): String {
        return jenkinsUrlTextField.text
    }

    fun setJenkinsUrl(url: String) {
        jenkinsUrlTextField.text = url
    }

    fun getTrustSelfSigned(): Boolean {
        return trustSelfSignedCheckbox.isSelected
    }

    fun setTrustSelfSigned(trustSelfSigned: Boolean) {
        trustSelfSignedCheckbox.isSelected = trustSelfSigned
    }

    fun getUseCrumbIssuer(): Boolean {
        return useCrumbIssuerCheckbox.isSelected
    }

    fun setUseCrumbIssuer(useCrumbIssuer: Boolean) {
        useCrumbIssuerCheckbox.isSelected = useCrumbIssuer
    }

    fun setCredentials(credentials: Credentials?) {
        if (credentials != null) {
            passwordTextField.text = credentials.getPasswordAsString()
            usernameTextField.text = credentials.userName ?: ""
        }
    }

    fun getUsername(): String? {
        return usernameTextField.text
    }

    fun getPassword(): CharArray? {
        return passwordTextField.password
    }

    fun getCredentials(): Credentials? {
        val username = getUsername()
        val password = getPassword()
        if (username != null && password != null) {
            return Credentials(username, password)
        }
        return null
    }

    fun getPanel(): JPanel {
        return panel
    }

    private fun checkConnection() {
        val url = jenkinsUrlTextField.text
        if (url.isNullOrBlank()) {
            Messages.showErrorDialog("Please provide Jenkins URL", "Jenkins Linter Configuration")
            return
        }
        val test = JenkinsCheckConnectionTask(
            jenkinsUrlTextField.text,
            trustSelfSignedCheckbox.isSelected, getCredentials()
        )
        ProgressManager.getInstance().run(test)
        if (test.success) {
            Messages.showInfoMessage(
                "Successfully connected!",
                "Jenkins Linter Configuration"
            )
        } else {
            Messages.showErrorDialog(
                "Failed to connect to the server. Please check the configuration.",
                "Jenkins Linter Configuration"
            )
        }
    }
}
