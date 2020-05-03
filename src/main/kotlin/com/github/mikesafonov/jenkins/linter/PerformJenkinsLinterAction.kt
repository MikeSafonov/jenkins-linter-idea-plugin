package com.github.mikesafonov.jenkins.linter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.impl.ToolWindowImpl

/**
 * @author Mike Safonov
 */

class PerformJenkinsLinterAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val fileContent = readFileContent(event)
        fileContent?.apply {
            val linterToolWindow = JenkinsLinterToolWindowFactory.getLinterToolWindow(event.project!!)
            if (linterToolWindow.isAvailable) {
                reactivateToolWindow(linterToolWindow)
            }
            val settings = JenkinsLinterSettings.getInstance()
            val credentials = JenkinsLinterCredentials.get()
            val linter = if (credentials != null) {
                JenkinsLinter(settings.jenkinsUrl, credentials.userName!!,
                        credentials.getPasswordAsString()!!, settings.useCrumb)
            } else {
                JenkinsLinter(settings.jenkinsUrl, settings.useCrumb)
            }

            val linterResponse = linter.lint(fileContent)
            Logger.getInstance(PerformJenkinsLinterAction::class.java).debug(linterResponse.message)
            JenkinsLinterToolWindowFactory.panel.add(linterResponse)
        }
    }

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = virtualFile != null
    }


    private fun readFileContent(event: AnActionEvent): String? {
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
        return if (virtualFile != null) {
            String(virtualFile.contentsToByteArray())
        } else null
    }

    private fun reactivateToolWindow(linterToolWindow: ToolWindow) {
        if (linterToolWindow is ToolWindowImpl) {
            linterToolWindow.ensureContentInitialized()
        }
        linterToolWindow.activate(null)
    }
}
