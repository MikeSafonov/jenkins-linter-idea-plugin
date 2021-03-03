package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.HttpCodes
import com.github.mikesafonov.jenkins.linter.JenkinsCrumb
import com.github.mikesafonov.jenkins.linter.JenkinsLinterException
import com.google.gson.Gson
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils
import java.nio.charset.StandardCharsets

/**
 * @author Mike Safonov
 */
class JenkinsCrumbIssuer(
    private val url: String,
    private val client: HttpClient
) {

    fun get(): JenkinsCrumb {
        val response = execute("$url/crumbIssuer/api/json")
        if (response.statusLine.statusCode == HttpCodes.SUCCESS) {
            return parseToCrumb(response.entity)
        } else {
            val code = response.statusLine.statusCode
            throw JenkinsLinterException(
                "Unable to get crumb. Reason: ${response.statusLine.reasonPhrase}", code
            )
        }
    }

    private fun execute(url: String): HttpResponse {
        val httpGet = HttpGet(url)
        return client.execute(httpGet)
    }

    private fun parseToCrumb(entity: HttpEntity): JenkinsCrumb {
        val stringContent = EntityUtils.toString(entity, StandardCharsets.UTF_8)
        return Gson().fromJson(stringContent, JenkinsCrumb::class.java)
    }
}
