package com.faangx.updater.core

import com.faangx.updater.util.Constants
import com.faangx.updater.util.listDirs
import com.faangx.updater.model.Task
import java.io.File

object ForkParser {

    fun parse(path: String): List<Task> {
        val root = File(path)
        val module = Constants.modules.first()
        val moduleRoot = root.resolve(module)
        val eps = moduleRoot.listDirs()

        return eps.map { ep ->
            val tasksRoot = ep.listDirs()
            tasksRoot.map { TaskParser.parseTask(path, it) }
        }.flatten()
    }

}