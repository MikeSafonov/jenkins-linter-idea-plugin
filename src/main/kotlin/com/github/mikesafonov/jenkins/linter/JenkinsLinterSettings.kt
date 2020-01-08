package com.github.mikesafonov.jenkins.linter

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


/**
 * @author Mike Safonov
 */
@State(name = "JenkinsLinterSettings", storages = [Storage("jenkins-linter.xml")])
class JenkinsLinterSettings : PersistentStateComponent<JenkinsLinterSettings> {
    var jenkinsUrl = ""
    var useCrumb = true

    companion object {
        fun getInstance() = ServiceManager.getService(JenkinsLinterSettings::class.java)!!
    }

    override fun getState(): JenkinsLinterSettings {
        return this
    }

    override fun loadState(state: JenkinsLinterSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}