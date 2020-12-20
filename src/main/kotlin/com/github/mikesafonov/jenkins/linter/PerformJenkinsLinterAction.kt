package com.github.mikesafonov.jenkins.linter

import com.github.mikesafonov.jenkins.linter.settings.JenkinsLinterState
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.wm.ToolWindow

/**
 * @author Mike Safonov
 */

class PerformJenkinsLinterAction : AnAction() {

    private val reader = FileContentReader()

    override fun actionPerformed(event: AnActionEvent) {
        val fileContent = reader.read(event)
        fileContent?.apply {
            val linterToolWindow = JenkinsLinterToolWindowFactory.getLinterToolWindow(event.project!!)
            if (linterToolWindow.isAvailable) {
                reactivateToolWindow(linterToolWindow)
            }
            val settings = JenkinsLinterState.getInstance()
            val linter = JenkinsLinter(settings.jenkinsUrl, false)
            val linterResponse = linter.lint(fileContent)
            Logger.getInstance(PerformJenkinsLinterAction::class.java).debug(linterResponse.message)
            JenkinsLinterToolWindowFactory.panel.add(linterResponse)
        }
    }

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = virtualFile != null
    }

    private fun reactivateToolWindow(linterToolWindow: ToolWindow) {
        linterToolWindow.activate(null)
    }
}
