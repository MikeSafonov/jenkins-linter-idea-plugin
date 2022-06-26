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
    val panel: JPanel
    private var jenkinsUrlTextField: JTextField = JTextField()
    private var trustSelfSignedCheckbox: JCheckBox = JCheckBox()
    private var ignoreCertificateCheckbox: JCheckBox = JCheckBox()
    private var verifyButton = JButton("Check connection")
    private var usernameTextField: JTextField = JTextField()
    private var passwordTextField: JPasswordField = JPasswordField()
    private var useCrumbIssuerCheckbox: JCheckBox = JCheckBox()

    var jenkinsUrl: String
        get() = jenkinsUrlTextField.text
        set(value) {
            jenkinsUrlTextField.text = value
        }
    var trustSelfSigned: Boolean
        get() = trustSelfSignedCheckbox.isSelected
        set(value) {
            trustSelfSignedCheckbox.isSelected = value
        }
    var ignoreCertificate: Boolean
        get() = ignoreCertificateCheckbox.isSelected
        set(value) {
            ignoreCertificateCheckbox.isSelected = value
        }
    var useCrumbIssuer: Boolean
        get() = useCrumbIssuerCheckbox.isSelected
        set(value) {
            useCrumbIssuerCheckbox.isSelected = value
        }
    var credentials: Credentials?
        get() {
            if (username != null && password != null) {
                return Credentials(username, password)
            }
            return null
        }
        set(value) {
            if (value != null) {
                passwordTextField.text = value.getPasswordAsString()
                usernameTextField.text = value.userName ?: ""
            }
        }
    val username: String?
        get() = usernameTextField.text
    val password: CharArray?
        get() = passwordTextField.password

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Jenkins url (protocol://hostname:port):"), jenkinsUrlTextField, 1, false)
            .addLabeledComponent(JBLabel("Trust self-signed:"), trustSelfSignedCheckbox, 1, false)
            .addLabeledComponent(JBLabel("Ignore certificate:"), ignoreCertificateCheckbox, 1, false)
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

    private fun checkConnection() {
        val url = jenkinsUrlTextField.text
        if (url.isNullOrBlank()) {
            Messages.showErrorDialog("Please provide Jenkins URL", "Jenkins Linter Configuration")
            return
        }
        val test = JenkinsCheckConnectionTask(
            jenkinsUrlTextField.text,
            trustSelfSignedCheckbox.isSelected, ignoreCertificateCheckbox.isSelected, credentials
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
