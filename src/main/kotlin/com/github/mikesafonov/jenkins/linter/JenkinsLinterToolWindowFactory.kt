package com.github.mikesafonov.jenkins.linter

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory

/**
 * @author Mike Safonov
 */

class JenkinsLinterToolWindowFactory : ToolWindowFactory, DumbAware {

    companion object {
        val panel = LinterResponsePanel()

        fun getLinterToolWindow(project: Project): ToolWindow {
            return ToolWindowManager.getInstance(project).getToolWindow("Jenkins Linter")
        }

    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val createContent = ContentFactory.SERVICE.getInstance().createContent(panel, "Jenkins Linter", false)
        toolWindow.contentManager.addContent(createContent)
    }
}