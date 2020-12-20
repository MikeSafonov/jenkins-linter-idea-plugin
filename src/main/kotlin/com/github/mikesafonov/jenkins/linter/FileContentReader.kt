package com.github.mikesafonov.jenkins.linter

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

/**
 * @author Mike Safonov
 */
class FileContentReader {

    fun read(event: AnActionEvent): String? {
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
        return if (virtualFile != null) {
            String(virtualFile.contentsToByteArray())
        } else null
    }
}
