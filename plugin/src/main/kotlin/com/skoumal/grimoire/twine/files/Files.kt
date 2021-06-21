package com.skoumal.grimoire.twine.files

import org.gradle.api.Project
import java.io.File

fun Project.generatedResDir() =
    buildDir("generated/res/twine")

fun Project.buildDir(name: String) =
    File(buildDir, "twine/$name").also {
        if (!it.isDirectory) {
            it.deleteRecursively()
            it.mkdirs()
        }
    }

fun Project.buildFile(name: String) =
    File(buildDir, "twine/$name").also {
        if (!it.isFile) {
            it.deleteRecursively()
            it.createNewFile()
        }
    }