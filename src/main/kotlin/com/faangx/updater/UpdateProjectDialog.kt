package com.faangx.updater

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel
import javax.swing.Action
import javax.swing.JComponent

class UpdateProjectDialog(
    project: Project?,
    private val latestVersion: String,
    private val currentVersion: String?,
    private val onConfirmUpdate: () -> Unit
) : DialogWrapper(project) {

    init {
        init()
        title = "Update KTP-Course"
        setSize(400, 200)
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row {
                label("Your projects requires an update!")
            }
            row {
                label("Latest version : $latestVersion")
            }
            row {
                label("Current version : $currentVersion")
            }
        }
    }

    override fun doOKAction() {
        close(CLOSE_EXIT_CODE)
        onConfirmUpdate()
    }
}