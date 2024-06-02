package com.faangx.updater.core

import com.github.syari.kgit.KGit
import java.io.File

object NewProjectSetupHelper {

    fun createNewProject(
        forkPath: String,
        repoPath: String,
        newProjectPath: String,
        latestVersion: String
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

        commitChangesOnNewProject(newRoot, latestVersion)
    }

    private fun commitChangesOnNewProject(newRoot: File, latestVersion: String) {
        val repo = KGit.open(newRoot)
        repo.add {
            addFilepattern(".")
        }
        repo.commit {
            message = "upgrade KTP-Course to $latestVersion"
        }
    }

}