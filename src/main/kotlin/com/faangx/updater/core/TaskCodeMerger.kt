package com.faangx.updater.core

import com.faangx.updater.model.TaskCode

object TaskCodeMerger {

    fun merge(taskCode: TaskCode, repoTaskCode: TaskCode): TaskCode {
        val taskImports = taskCode.imports
            .split("\n")
            .filterNot { it.contains("MiniApp") }
            .toSet()

        val repoImports = repoTaskCode.imports
            .split("\n")
            .toSet()

        val mergedImports = taskImports union repoImports

        return repoTaskCode.copy(
            imports = mergedImports.joinToString("\n"),
            funs = taskCode.funs
        )
    }

}