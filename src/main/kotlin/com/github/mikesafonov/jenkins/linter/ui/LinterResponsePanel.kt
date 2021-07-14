package com.github.mikesafonov.jenkins.linter.ui

import com.github.mikesafonov.jenkins.linter.ScriptErrorData
import com.github.mikesafonov.jenkins.linter.ToSourceMover
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBTextArea
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

/**
 * @author Mike Safonov
 */

class LinterResponsePanel(val mover: ToSourceMover) : JBPanelWithEmptyText() {

    init {
        this.layout = BorderLayout()
    }

    fun setText(errorText: String) {
        removeAll()
        withEmptyText(errorText)
        updateUI()
    }

    fun multilineText(errorText: String) {
        removeAll()
        add(ScrollPaneFactory.createScrollPane(JBTextArea(errorText)), BorderLayout.CENTER)
        updateUI()
    }

    fun setErrors(errors: List<ScriptErrorData>) {
        removeAll()
        add(ScrollPaneFactory.createScrollPane(buildList(errors)), BorderLayout.CENTER)
        updateUI()
    }

    private fun buildList(errors: List<ScriptErrorData>): JBList<ScriptErrorData> {
        val list = JBList<ScriptErrorData>()
        list.model = CollectionListModel(errors)
        list.cellRenderer = ScriptErrorCellRenderer()
        list.addMouseListener(
            object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    if (e == null || e.clickCount < 2) {
                        return
                    }
                    val selected = list.selectedValue
                    mover.move(selected)
                }
            }
        )
        return list
    }
}
