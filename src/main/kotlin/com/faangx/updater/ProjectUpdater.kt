package com.faangx.updater

import com.faangx.updater.core.ForkParser
import com.faangx.updater.core.NewProjectSetupHelper
import com.faangx.updater.core.RepoCloner
import com.faangx.updater.core.RepoUpdater
import com.faangx.updater.util.basePath
import com.intellij.openapi.project.Project
import java.io.File

object ProjectUpdater {

    fun updateProject(project: Project, newProjectPath: String, latestVersion: String) {
        val forkPath = project.basePath()
        val repoFolder = File(forkPath).parentFile.resolve("KTP-Course-Main-Temp")
        repoFolder.deleteRecursively()
        val repoPath = repoFolder.path

        RepoCloner.clone(repoPath)
        val tasks = ForkParser.parse(forkPath)

        RepoUpdater.updateRepo(repoPath, tasks)

        NewProjectSetupHelper.createNewProject(forkPath, repoPath, newProjectPath, latestVersion)
    }

}