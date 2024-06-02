package com.faangx.updater.core

import com.faangx.updater.model.Task
import com.faangx.updater.util.resolveCodeFile
import com.faangx.updater.util.resolveTaskInfoFile
import java.io.File

object RepoUpdater {

    fun updateRepo(
        path: String,
        tasks: List<Task>
    ) {
        val repoRoot = File(path)
        tasks.forEach {
            updateTaskInRepo(it, repoRoot, path)
        }
    }

    private fun updateTaskInRepo(task: Task, repoRoot: File, repoPath: String) {
        val taskRoot = repoRoot.resolve(task.path)
        val repoTask = TaskParser.parseTask(repoPath, taskRoot)
        val mergedTaskCode = TaskCodeMerger.merge(task.taskCode, repoTask.taskCode)
        taskRoot.resolveCodeFile().writeText(mergedTaskCode.content())
        if (repoTask.testChecksum == task.testChecksum) {
            println("updated taskInfo for ${task.path}")
            taskRoot.resolveTaskInfoFile().writeText(task.taskInfo)
        }
    }

}