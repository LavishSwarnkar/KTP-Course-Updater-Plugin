package com.faangx.updater.model

data class Task(
    val path: String,
    val taskCode: TaskCode,
    val taskInfo: String,
    val testChecksum: String
)
