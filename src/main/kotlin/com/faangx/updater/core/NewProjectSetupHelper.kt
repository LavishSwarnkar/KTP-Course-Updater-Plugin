package com.faangx.updater.core

import java.io.File

object NewProjectSetupHelper {

    fun createNewProject(
        forkPath: String,
        repoPath: String,
        newProjectPath: String
    ) {
        val forkRoot = File(forkPath)
        val repoRoot = File(repoPath)
        val newRoot = File(newProjectPath)

        // Duplicate repo
        repoRoot.copyRecursively(newRoot)

        // Delete .git folder
        newRoot.resolve(".git").deleteRecursively()

        // Copy fork's .git folder
        forkRoot.resolve(".git")
            .copyRecursively(
                newRoot.resolve(".git")
            )

        // Delete cloned repo
        repoRoot.deleteRecursively()
    }

}