package com.faangx.updater

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel
import java.awt.Label
import javax.swing.Action
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel

class UpdateProjectDialog(
    project: Project?,
    val latestVersion: String
) : DialogWrapper(project) {

    init {
        init()
        title = "Update KTP-Course"
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row {
                label("Latest version : $latestVersion")
            }
            row {
                button("Ok") {
                    close(0)
                }
            }
        }
    }

    override fun createActions(): Array<Action> {
        return emptyArray()
    }
}