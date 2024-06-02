package com.faangx.updater.core

import java.io.File

object NewProjectSetupHelper {

    fun createNewProject(
        forkPath: String,
        repoPath: String
    ) {
        val forkRoot = File(forkPath)
        val repoRoot = File(repoPath)
        val backupRoot = forkRoot.parentFile.resolve("KTP-Course-Backup")

        // Backup
        forkRoot.copyRecursively(backupRoot)

        // Delete fork
        forkRoot.deleteRecursively()

        // Duplicate repo
        repoRoot.copyRecursively(forkRoot)

        // Delete .git folder
        forkRoot.resolve(".git").deleteRecursively()

        // Copy fork's .git folder
        backupRoot.resolve(".git")
            .copyRecursively(
                forkRoot.resolve(".git")
            )

        // Delete cloned repo
        repoRoot.deleteRecursively()
    }

}