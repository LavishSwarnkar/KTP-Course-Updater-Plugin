package com.faangx.updater.core

import com.faangx.updater.core.TaskParser.ParsePhase.*
import com.faangx.updater.model.Task
import com.faangx.updater.model.TaskCode
import com.faangx.updater.util.resolveCodeFile
import com.faangx.updater.util.resolveTaskInfoFile
import org.yaml.snakeyaml.Yaml
import java.io.File

object TaskParser {

    fun parseTask(projectPath: String, root: File): Task {
        val codeFile = root.resolveCodeFile()
        val taskInfo = root.resolveTaskInfoFile().readText()

        return Task(
            path = root.path.replace(projectPath, "").trim('/', '\\'),
            taskCode = parseTaskCode(codeFile),
            taskInfo = taskInfo,
            testChecksum = getTestSignatureFromTaskInfo(taskInfo)
        )
    }

    private enum class ParsePhase { Imports, Funs, MainFun }

    private fun parseTaskCode(file: File): TaskCode {
        val lines = file.readLines()

        val imports = mutableListOf<String>()
        val funs = mutableListOf<String>()
        val mainFun = mutableListOf<String>()

        var phase = Imports

        for (line in lines) {
            when (phase) {
                Imports -> {
                    if (line.startsWith("fun")) {
                        phase = Funs
                        funs.add(line)
                    } else {
                        imports.add(line)
                    }
                }
                Funs -> {
                    if (line.startsWith("fun main")) {
                        phase = MainFun
                        mainFun.add(line)
                    } else {
                        funs.add(line)
                    }
                }
                MainFun -> {
                    mainFun.add(line)
                }
            }
        }

        return TaskCode(
            imports = imports.joinToString("\n").trim('\n'),
            funs = funs.joinToString("\n").trim('\n'),
            mainFun = mainFun.joinToString("\n").trim('\n')
        )
    }

    private fun getTestSignatureFromTaskInfo(taskInfo: String): String {
        val yaml = Yaml().load<Map<String, Any>>(taskInfo)
        val files = yaml["files"] as? List<Map<String, Any>> ?: error("Unable to parse files")
        val testFileInfo = files.find { it["name"] == "test/Tests.kt" } ?: error("Unable to get testFileInfo")
        val checksum = testFileInfo["encrypted_text"] as? String ?: error("testChecksum not found")
        return checksum
    }

}