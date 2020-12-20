package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.JenkinsCrumb
import com.google.gson.Gson
import org.apache.http.HttpEntity
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.util.EntityUtils
import java.nio.charset.StandardCharsets

/**
 * @author Mike Safonov
 */
class JenkinsCrumbIssuer(private val url: String, private val client: HttpClient,
                         private val context: HttpClientContext?) {

    fun get(): JenkinsCrumb {
        val crumbUrl = "${url}/crumbIssuer/api/json"

        val httpGet = HttpGet(crumbUrl)
        val response = if (context != null) {
            client.execute(httpGet, context)
        } else {
            client.execute(httpGet)
        }
        if (response.statusLine.statusCode == 200) {
            return parseToCrumb(response.entity)
        } else {
            throw RuntimeException("Unable to get crumb. Http code: " + response.statusLine.statusCode + " Reason: "
                    + response.statusLine.reasonPhrase)
        }
    }

    private fun parseToCrumb(entity: HttpEntity): JenkinsCrumb {
        val stringContent = EntityUtils.toString(entity, StandardCharsets.UTF_8)
        return Gson().fromJson(stringContent, JenkinsCrumb::class.java)
    }
}
