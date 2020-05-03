package com.github.mikesafonov.jenkins.linter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.wm.impl.ToolWindowImpl

/**
 * @author Mike Safonov
 */

class PerformJenkinsLinterAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val fileContent = readFileContent(event)
        if (fileContent != null) {
            val linterToolWindow = JenkinsLinterToolWindowFactory.getLinterToolWindow(event.project!!)
            if (linterToolWindow.isAvailable) {
                if (linterToolWindow is ToolWindowImpl) {
                    linterToolWindow.ensureContentInitialized()
                }
                linterToolWindow.activate(null)
            }
            val linterResponse = JenkinsLinter().lint(fileContent)
            println(linterResponse.message)
            JenkinsLinterToolWindowFactory.panel.add(linterResponse)
        } else {
            println("No data to validate")
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
}
