package com.faangx.updater.core

import com.intellij.openapi.project.Project
import java.io.File

object CurrentProjectChecker {

    fun check(project: Project): String? {
        val basePath = project.basePath ?: error("Unable to get project basePath")
        val root = File(basePath)
        val courseInfoFile = root.resolve("course-info.yaml")
        if (!courseInfoFile.exists()) error("Current project is not a KTP-Course Project")
        return CourseVersionHelper.parseVersion(courseInfoFile.readText())
    }

}