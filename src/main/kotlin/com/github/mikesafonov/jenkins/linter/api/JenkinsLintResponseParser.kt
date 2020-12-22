package com.github.mikesafonov.jenkins.linter.api

import com.github.mikesafonov.jenkins.linter.ScriptError
import java.util.regex.Pattern

/**
 * @author Mike Safonov
 */
class JenkinsLintResponseParser {
    private val errorPattern: Pattern = Pattern.compile("@ line ([0-9]+), column ([0-9]+)")

    fun parse(content: String): List<ScriptError> {
        return content.lines().mapNotNull {
            toScriptError(it)
        }.asReversed()
    }

    private fun toScriptError(line: String): ScriptError? {
        val matcher = errorPattern.matcher(line)
        if (!matcher.find()) {
            return null
        }

        return ScriptError(matcher.group(1).toInt(), matcher.group(2).toInt(), line)
    }
}
