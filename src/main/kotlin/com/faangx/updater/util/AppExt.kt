package com.faangx.updater.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.nio.channels.UnresolvedAddressException

fun runOnMain(lambda: () -> Unit) {
    ApplicationManager.getApplication().invokeLater {
        execute { lambda() }
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

                execute { lambda() }
            }
        }
    )
}

fun Project.basePath(): String {
    return basePath ?: error("Unable to get project basePath")
}

fun execute(lambda: () -> Unit) {
    try {
        lambda()
    } catch (e: Exception) {
        runOnMain {
            Messages.showErrorDialog(
                "${e.message}",
                "Error"
            )
        }
    }
}

suspend fun suspendExecute(lambda: suspend () -> Unit) {
    try {
        lambda()
    } catch (e: Exception) {
        val message = when (e) {
            is UnresolvedAddressException -> "You are offline. Please try again after connecting to the internet."
            else -> e.message.toString()
        }
        runOnMain {
            Messages.showErrorDialog(
                message,
                "Error"
            )
        }
    }
}