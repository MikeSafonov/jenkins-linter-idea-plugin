package com.github.mikesafonov.jenkins.linter

import javax.swing.JLabel
import javax.swing.JPanel

/**
 * @author Mike Safonov
 */

class LinterResponsePanel : JPanel() {
    private val label = JLabel("")

    init {
        add(label)
    }


    fun add(linterResponse: LinterResponse) {
        println(linterResponse)
        label.text = linterResponse.message
    }

}