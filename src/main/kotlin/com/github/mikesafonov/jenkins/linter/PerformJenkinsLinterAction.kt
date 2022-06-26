package com.github.mikesafonov.jenkins.linter

import com.github.mikesafonov.jenkins.linter.api.JenkinsLintResponseParser
import com.github.mikesafonov.jenkins.linter.api.JenkinsServerFactory
import com.github.mikesafonov.jenkins.linter.settings.JenkinsLinterState
import com.github.mikesafonov.jenkins.linter.ui.LinterResponsePanel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
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
            val settings = JenkinsLinterState.getInstance()
            val panel = JenkinsLinterToolWindowFactory.getPanel(project)
            if (settings.jenkinsUrl.isBlank()) {
                panel.setText(
                    "Please configure Jenkins instance under Settings | Tools | Jenkins Linter"
                )
            } else {
                ProgressManager.getInstance()
                    .run(object : Backgroundable(project, "Linting ${fileContent.name} ...", false) {
                        override fun run(indicator: ProgressIndicator) {
                            val linterResponse = doLint(fileContent.content, settings)
                            ApplicationManager.getApplication().invokeLaterOnWriteThread {
                                when (linterResponse.code) {
                                    HttpCodes.FORBIDDEN -> {
                                        panel.setText(
                                            """
                                    Forbidden. Please configure Jenkins instance under Settings | Tools | Jenkins Linter
                                            """.trimIndent()
                                        )
                                    }
                                    HttpCodes.SUCCESS -> {
                                        handleSuccess(linterResponse, panel, event)
                                    }
                                    else -> {
                                        val mess = linterResponse.message
                                        val code = linterResponse.code
                                        panel.setText("HTTP response status code: $code, message: $mess")
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }

    private fun handleSuccess(
        linterResponse: LinterResponse,
        panel: LinterResponsePanel,
        event: AnActionEvent
    ) {
        if (linterResponse.successValidated) {
            panel.setText(linterResponse.message)
        } else {
            val errors = JenkinsLintResponseParser().parse(linterResponse.message)
            if (errors.isEmpty()) {
                panel.multilineText(linterResponse.message)
            } else {
                val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)!!
                panel.setErrors(errors.map { ScriptErrorData(virtualFile, it) })
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = virtualFile != null && !virtualFile.isDirectory
    }

    private fun doLint(content: String, settings: JenkinsLinterState): LinterResponse {
        val linter = JenkinsServerFactory.get(
            settings.jenkinsUrl, settings.trustSelfSigned, settings.ignoreCertificate,
            settings.useCrumbIssuer
        )
        linter.use {
            val linterResponse = it.lint(content)
            Logger.getInstance(PerformJenkinsLinterAction::class.java).debug(linterResponse.message)
            return linterResponse
        }
    }

    private fun reactivateToolWindow(linterToolWindow: ToolWindow) {
        linterToolWindow.activate(null)
    }
}
