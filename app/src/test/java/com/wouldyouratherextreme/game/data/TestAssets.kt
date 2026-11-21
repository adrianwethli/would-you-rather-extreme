package com.wouldyouratherextreme.game.data

import java.io.File

/** Finds the real question files that ship inside the app, so tests can check them. */
internal object TestAssets {

    // Gradle runs unit tests from the app folder; the second path covers running from the repo root.
    private val folder: File = listOf(
        "src/main/assets/questions",
        "app/src/main/assets/questions",
    ).map(::File).firstOrNull { it.isDirectory }
        ?: error("Could not find the questions folder")

    fun read(name: String): String = File(folder, name).readText(Charsets.UTF_8)

    /** Names of all JSON files in the questions folder. */
    fun jsonFileNames(): List<String> =
        (folder.list() ?: emptyArray()).filter { it.endsWith(".json") }.sorted()
}
