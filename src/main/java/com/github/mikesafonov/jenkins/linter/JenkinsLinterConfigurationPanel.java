package com.github.mikesafonov.jenkins.linter;

import com.intellij.credentialStore.Credentials;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.Comparing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Mike Safonov
 */
public class JenkinsLinterConfigurationPanel implements SearchableConfigurable {
    private JTextField jenkinsUrlTF;
    private JCheckBox useCrumbCB;
    private JPanel jpanel;
    private JTextField usernameTF;
    private JPasswordField passwordTF;
    private JenkinsLinterSettings jenkinsLinterSettings;

    @Nullable
    @Override
    public JComponent createComponent() {
        this.jenkinsLinterSettings = JenkinsLinterSettings.Companion.getInstance();
        reset();
        return jpanel;
    }

    @Override
    public boolean isModified() {
        Credentials credentials = JenkinsLinterCredentials.INSTANCE.get();
        if (credentials != null) {
            return !Comparing.equal(jenkinsUrlTF.getText(), jenkinsLinterSettings.getJenkinsUrl())
                    || !Comparing.equal(usernameTF.getText(), credentials.getUserName())
                    || !Comparing.equal(passwordTF.getPassword(), credentials.getPasswordAsString())
                    || !Comparing.equal(useCrumbCB.isSelected(), jenkinsLinterSettings.getUseCrumb());
        } else {
            return !Comparing.equal(jenkinsUrlTF.getText(), jenkinsLinterSettings.getJenkinsUrl())
                    || !Comparing.equal(useCrumbCB.isSelected(), jenkinsLinterSettings.getUseCrumb());
        }
    }

    @Override
    public void apply() {
        jenkinsLinterSettings.setJenkinsUrl(jenkinsUrlTF.getText());
        jenkinsLinterSettings.setUseCrumb(useCrumbCB.isSelected());
        JenkinsLinterCredentials.INSTANCE.store(usernameTF.getText(), passwordTF.getPassword());
    }

    @Override
    public void reset() {
        jenkinsUrlTF.setText(jenkinsLinterSettings.getJenkinsUrl());
        useCrumbCB.setSelected(jenkinsLinterSettings.getUseCrumb());
        Credentials credentials = JenkinsLinterCredentials.INSTANCE.get();
        if (credentials != null) {
            usernameTF.setText(credentials.getUserName());
            passwordTF.setText(credentials.getPasswordAsString());
        }
    }


    @NotNull
    @Override
    public String getId() {
        return "Jenkins linter";
    }

    @Override
    public String getDisplayName() {
        return "Jenkins Linter";
    }
}
