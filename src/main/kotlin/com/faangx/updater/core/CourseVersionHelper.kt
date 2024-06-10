package com.faangx.updater.core

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.yaml.snakeyaml.Yaml

class CourseVersionHelper(
    private val client: HttpClient
) {
    class VersionResult(
        val latestCourseVersion: String,
        val latestUpdaterPluginVersion: String
    )

    suspend fun getLatestCourseVersion(): VersionResult {
        val courseInfoUrl = "https://raw.githubusercontent.com/The-Streamliners/KTP-Course/main/course-info.yaml"

        val data = client.get(courseInfoUrl).bodyAsText()
        val yaml = Yaml().load<Map<String, Any>>(data)
        val tags = yaml["tags"] as? List<String> ?: error("tags not found")

        return VersionResult(
            latestCourseVersion = tags.firstOrNull()
                ?: error("Version not found in main repo"),
            latestUpdaterPluginVersion = tags.getOrNull(1)
                ?: error("UpdaterPluginVersion not found in main repo")
        )
    }

    companion object {
        fun parseVersion(data: String): String? {
            val yaml = Yaml().load<Map<String, Any>>(data)
            return (yaml["tags"] as? List<String>)?.firstOrNull()
        }
    }

}