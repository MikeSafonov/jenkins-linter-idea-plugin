package com.github.mikesafonov.jenkins.linter

import com.github.mikesafonov.jenkins.linter.ui.LinterResponsePanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ToolWindowType

/**
 * @author Mike Safonov
 */

class JenkinsLinterToolWindowFactory : ToolWindowFactory {

    companion object {
        fun getLinterToolWindow(project: Project): ToolWindow {
            return ToolWindowManager.getInstance(project).getToolWindow("Jenkins Linter")!!
        }

        fun getPanel(project: Project) : LinterResponsePanel {
            val content = getLinterToolWindow(project).contentManager.getContent(0)!!
            return content.component as LinterResponsePanel
        }

    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val mover = ToSourceMover(project)
        val createContent = toolWindow.contentManager.factory.createContent(
            LinterResponsePanel(mover),
            "Jenkins Linter", false
        )
        toolWindow.contentManager.addContent(createContent)
        toolWindow.title = "Jenkins Linter"
        toolWindow.setType(ToolWindowType.DOCKED, null)
    }
}
