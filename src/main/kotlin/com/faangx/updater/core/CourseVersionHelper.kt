package com.faangx.updater.core

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.yaml.snakeyaml.Yaml

class CourseVersionHelper(
    private val client: HttpClient
) {

    suspend fun getLatestCourseVersion(): String {
        val courseInfoUrl = "https://raw.githubusercontent.com/The-Streamliners/KTP-Course/main/course-info.yaml"
        return parseCourseInfoAndGetVersion(courseInfoUrl)
            ?: error("Version not found in main repo")
    }

    private suspend fun parseCourseInfoAndGetVersion(url: String): String? {
        val data = client.get(url).bodyAsText()
        val yaml = Yaml().load<Map<String, Any>>(data)
        return (yaml["tags"] as? List<String>)?.firstOrNull()
    }

}