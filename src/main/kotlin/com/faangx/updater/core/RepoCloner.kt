package com.faangx.updater.core

import com.github.syari.kgit.KGit
import org.eclipse.jgit.lib.TextProgressMonitor
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

object RepoCloner {

    fun clone(path: String) {
        KGit.cloneRepository {
            setURI("https://github.com/The-Streamliners/KTP-Course")
            setTimeout(60)
            setProgressMonitor(TextProgressMonitor())
            setDirectory(
                File(path)
            )
        }
    }

}