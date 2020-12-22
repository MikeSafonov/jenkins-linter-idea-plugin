package com.github.mikesafonov.jenkins.linter.ui

import com.github.mikesafonov.jenkins.linter.ScriptErrorData
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JLabel
import javax.swing.JList

/**
 * @author Mike Safonov
 */
class ScriptErrorCellRenderer : DefaultListCellRenderer() {

    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        if (value != null && value is ScriptErrorData) {
            val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel
            component.text = "(${value.error.line};${value.error.column}) ${value.error.message}"
            return component
        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel
    }
}
