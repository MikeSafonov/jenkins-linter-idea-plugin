package com.github.mikesafonov.jenkins.linter

import com.github.mikesafonov.jenkins.linter.api.JenkinsLintResponseParser
import com.github.mikesafonov.jenkins.linter.api.JenkinsServer
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
            val project = event.project!!
            val linterToolWindow = JenkinsLinterToolWindowFactory.getLinterToolWindow(event.project!!)
            if (linterToolWindow.isAvailable) {
                reactivateToolWindow(linterToolWindow)
            }
            val linterResponse = doLint(fileContent)

            val errors = JenkinsLintResponseParser().parse(linterResponse.message)

            val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)!!
            JenkinsLinterToolWindowFactory.getPanel(project).setErrors(errors.map { ScriptErrorData(virtualFile, it) })
        }
    }

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = virtualFile != null && !virtualFile.isDirectory
    }

    private fun doLint(content: String): LinterResponse {
        val settings = JenkinsLinterState.getInstance()
        val linter = JenkinsServer(settings.jenkinsUrl)
        val linterResponse = linter.lint(content)
        Logger.getInstance(PerformJenkinsLinterAction::class.java).debug(linterResponse.message)
        return linterResponse
    }

    private fun reactivateToolWindow(linterToolWindow: ToolWindow) {
        linterToolWindow.activate(null)
    }
}
