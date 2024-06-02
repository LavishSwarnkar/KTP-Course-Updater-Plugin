package com.faangx.updater.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

fun runOnMain(lambda: () -> Unit) {
    ApplicationManager.getApplication().invokeLater {
        lambda()
    }
}

fun showProgressIndicatorDialog(
    project: Project,
    title: String,
    message: String,
    lambda: () -> Unit
) {
    ProgressManager.getInstance().run(
        object : Task.Modal(project, title, true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = message

                lambda()
            }
        }
    )
}

fun Project.basePath(): String {
    return basePath ?: error("Unable to get project basePath")
}