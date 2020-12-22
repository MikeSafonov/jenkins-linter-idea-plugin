package com.github.mikesafonov.jenkins.linter

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project

/**
 * @author Mike Safonov
 */
class ToSourceMover(private val project: Project) {

    fun move(data: ScriptErrorData) {
        val descriptor = OpenFileDescriptor(project, data.file)
        val editor = FileEditorManager.getInstance(project).openTextEditor(descriptor, true)!!
        val line = data.error.line - 1
        val column = data.error.column - 1
        editor.caretModel.moveToLogicalPosition(LogicalPosition(line, column))
    }
}
