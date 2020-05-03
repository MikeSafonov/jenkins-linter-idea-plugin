package com.github.mikesafonov.jenkins.linter

import javax.swing.JPanel
import javax.swing.JTextPane

/**
 * @author Mike Safonov
 */

class LinterResponsePanel : JPanel() {
    private val textPane = JTextPane()

    init {
        textPane.isEditable = false
        add(textPane)
    }


    fun add(linterResponse: LinterResponse) {
        val message = if (linterResponse.success) {
            linterResponse.message
        } else {
            """Response code: ${linterResponse.code}
               Message: ${linterResponse.message}
            """.trimIndent()
        }
        textPane.text = message
    }
}
