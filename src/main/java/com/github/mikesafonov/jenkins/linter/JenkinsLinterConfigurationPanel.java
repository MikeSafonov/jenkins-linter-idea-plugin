package com.github.mikesafonov.jenkins.linter;

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
    private JPanel jpanel;
    private JenkinsLinterSettings jenkinsLinterSettings;

    @Nullable
    @Override
    public JComponent createComponent() {
        this.jenkinsLinterSettings = JenkinsLinterSettings.Companion.getInstance();
        return jpanel;
    }

    @Override
    public boolean isModified() {
        return !Comparing.equal(jenkinsUrlTF.getText(), jenkinsLinterSettings.getJenkinsUrl());
    }

    @Override
    public void apply() {
        jenkinsLinterSettings.setJenkinsUrl(jenkinsUrlTF.getText());
    }

    @Override
    public void reset() {
        jenkinsUrlTF.setText(jenkinsLinterSettings.getJenkinsUrl());
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
