package com.github.mikesafonov.jenkins.linter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

/**
 * @author Mike Safonov
 */

class PerformJenkinsLinterAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)!!
        val fileContent = String(virtualFile.contentsToByteArray())
        val jenkinsLinter = JenkinsLinter(JenkinsLinterSettings(
                "", "", ""
        ))
        jenkinsLinter.lint(fileContent)
    }

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = virtualFile?.name == "Jenkinsfile"
    }
}