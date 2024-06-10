package com.faangx.updater

import com.faangx.updater.core.CourseVersionHelper
import com.faangx.updater.core.CurrentProjectChecker
import com.faangx.updater.util.Constants.PLUGIN_ID
import com.faangx.updater.util.basePath
import com.faangx.updater.util.runOnMain
import com.faangx.updater.util.showProgressIndicatorDialog
import com.faangx.updater.util.suspendExecute
import com.intellij.ide.plugins.PluginManagerConfigurable
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.runBlocking
import java.io.File

class UpdateProjectAction : AnAction() {

    private val httpClient = HttpClient(CIO) { expectSuccess = true }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        showProgressIndicatorDialog(
            project, "Update KTP-Course", "Fetching latest version..."
        ) {
            runBlocking {
                suspendExecute {
                    checkProject(project)
                }
            }
        }
    }

    private suspend fun checkProject(project: Project) {
        val versionResult = CourseVersionHelper(httpClient).getLatestCourseVersion()
        val latestVersion = versionResult.latestCourseVersion
        val latestUpdaterPluginVersion = versionResult.latestUpdaterPluginVersion

        // Check Plugin version
        val currentPluginVersion = PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID))?.version
        if ("v$currentPluginVersion" != latestUpdaterPluginVersion) {
            runOnMain {
                PluginUpdateDialog(project, latestUpdaterPluginVersion).show()
            }
            return
        }

        // Check project version
        val currentVersion = CurrentProjectChecker.check(project) ?: "v1.0"

        if (currentVersion == latestVersion) {
            runOnMain {
                Messages.showInfoMessage(
                    "Your project is already on the latest version : $latestVersion",
                    "Up-To-Date"
                )
            }
            return
        }

        runOnMain {
            UpdateProjectDialog(project, latestVersion, currentVersion) {
                updateProject(project, latestVersion)
            }.show()
        }
    }

    private fun updateProject(project: Project, latestVersion: String) {
        runOnMain {
            showProgressIndicatorDialog(
                project, "Update KTP-Course", "Updating project..."
            ) {
                val newProjectPath = File(project.basePath())
                    .parentFile
                    .resolve("KTP-Course-$latestVersion")
                    .path

                ProjectUpdater.updateProject(project, newProjectPath, latestVersion)

                runOnMain {
                    Messages.showInfoMessage(
                        "Your project has been updated to the latest version : $latestVersion",
                        "Update Successful"
                    )

                    ProjectManager.getInstance().apply {
                        closeAndDispose(project)
                        loadAndOpenProject(newProjectPath)
                    }
                }
            }
        }
    }
}