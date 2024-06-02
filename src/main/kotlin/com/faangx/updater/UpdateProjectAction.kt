package com.faangx.updater

import com.faangx.updater.core.CourseVersionHelper
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.ui.Messages
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UpdateProjectAction : AnAction() {

    val httpClient = HttpClient(CIO) { expectSuccess = true }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        ProgressManager.getInstance().run(object : Task.Modal(project, "Processing", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = "Fetching latest version..."

                val version = runBlocking {
                    CourseVersionHelper(httpClient).getLatestCourseVersion()
                }

                ApplicationManager.getApplication().invokeLater {
                    val dialog = UpdateProjectDialog(project, version)
                    dialog.show()
                }
            }
        })
    }
}