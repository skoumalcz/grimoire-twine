package com.skoumal.grimoire.twine.files

import org.gradle.api.Project
import java.io.File

fun Project.generatedResDir() =
    generatedDir("res/twine")

// ---

fun Project.generatedDir(name: String) =
    File(buildDir, "generated/$name").requireDirectory()

fun Project.buildDir(name: String) =
    File(buildDir, "twine/$name").requireDirectory()

fun Project.buildFile(name: String) =
    File(buildDir, "twine/$name").requireFile()

// ---

fun File.requireDirectory() = apply {
    if (!isDirectory) {
        deleteRecursively()
        mkdirs()
    }
}

fun File.requireFile() = apply {
    parentFile.requireDirectory()
    if (!isFile) {
        deleteRecursively()
        createNewFile()
    }
}