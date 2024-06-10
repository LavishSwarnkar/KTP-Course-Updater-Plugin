package com.faangx.updater

import com.intellij.ide.plugins.PluginManagerConfigurable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel
import javax.swing.Action
import javax.swing.JComponent

class PluginUpdateDialog(
    private val project: Project?,
    private val latestPluginVersion: String
) : DialogWrapper(project) {

    init {
        init()
        title = "Plugin Update Required"
        setSize(400, 150)
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row {
                label(
                    "Plugin must be updated to the latest version : $latestPluginVersion"
                )
            }
        }
    }

    override fun createActions(): Array<Action> {
        return arrayOf(okAction, cancelAction).apply {
            okAction.putValue(Action.NAME, "Update")
        }
    }

    override fun doOKAction() {
        close(CLOSE_EXIT_CODE)
        ApplicationManager.getApplication().invokeLater {
            val configurable = PluginManagerConfigurable()
            ShowSettingsUtil.getInstance().editConfigurable(project, configurable) {
                configurable.openMarketplaceTab("KTP-Course-Updater")
            }
        }
    }
}