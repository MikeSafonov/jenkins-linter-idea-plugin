package com.github.mikesafonov.jenkins.linter.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * @author Mike Safonov
 */
@State(name = "JenkinsLinterState", storages = [Storage("JenkinsLinter.xml")])
class JenkinsLinterState : PersistentStateComponent<JenkinsLinterState> {
    var jenkinsUrl = ""

    companion object {
        fun getInstance() = ServiceManager.getService(JenkinsLinterState::class.java)!!
    }

    override fun getState(): JenkinsLinterState {
        return this
    }

    override fun loadState(state: JenkinsLinterState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
