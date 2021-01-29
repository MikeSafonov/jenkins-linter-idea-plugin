package com.github.mikesafonov.jenkins.linter.settings

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * @author Mike Safonov
 */
class JenkinsLinterComponent {
    private var panel: JPanel
    private var jenkinsUrlTextField: JTextField = JTextField()
    private var trustSelfSignedCheckbox : JCheckBox = JCheckBox()
    private var verifyButton = JButton("Check connection")

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Jenkins url (protocol://hostname:port):"), jenkinsUrlTextField, 1, false)
            .addLabeledComponent(JBLabel("Trust self-signed:"), trustSelfSignedCheckbox, 1, false)
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

    fun getPanel(): JPanel {
        return panel
    }

    private fun checkConnection() {
        val url = jenkinsUrlTextField.text
        if (url.isNullOrBlank()) {
            Messages.showErrorDialog("Please provide Jenkins URL", "Jenkins Linter Configuration")
            return
        }
        val test = JenkinsCheckConnectionTask(jenkinsUrlTextField.text, trustSelfSignedCheckbox.isSelected)
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
