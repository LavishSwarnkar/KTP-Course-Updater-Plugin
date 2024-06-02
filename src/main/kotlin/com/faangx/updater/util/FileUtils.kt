package com.faangx.updater.util

import java.io.File

fun File.listDirs(): List<File> {
    return this.listFiles { file -> file.isDirectory }
        ?.toList()
        ?: emptyList()
}

fun File.resolveCodeFile(): File {
    return resolve("src").resolve("Task.kt")
}

fun File.resolveTaskInfoFile(): File {
    return resolve("task-info.yaml")
}