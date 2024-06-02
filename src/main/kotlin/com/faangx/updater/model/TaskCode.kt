package com.faangx.updater.model

data class TaskCode(
    val imports: String,
    val funs: String,
    val mainFun: String
) {
    fun content(): String {
        return listOf(imports, funs, mainFun)
            .filterNot { it.isBlank() }
            .joinToString("\n\n")
    }
}
