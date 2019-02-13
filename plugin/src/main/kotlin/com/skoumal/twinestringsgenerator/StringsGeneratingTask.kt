package com.skoumal.twinestringsgenerator

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class StringsGeneratingTask : DefaultTask() {

    @get:InputFile
    lateinit var sourceFile: File

    @get:OutputDirectory
    lateinit var outputDir: File

    @get:Input
    lateinit var defaultLanguage: String
    @get:Input
    lateinit var outputFileName: String
    @get:Input
    lateinit var format: String
    @get:Input
    lateinit var args: List<String>

    @TaskAction
    fun execute() {
        println("> Generating strings from ${sourceFile.absolutePath}")

        try {

            val command = listOf(
                "twine", "generate-all-localization-files",
                "-n$outputFileName",
                "-f$format",
                "-r"
            ) + args + listOf(
                sourceFile.absolutePath, outputDir.absolutePath
            )

            project.exec {
                it.commandLine(command)
            }

            project.file("${outputDir.absolutePath}/values-$defaultLanguage")
                .renameTo(File(outputDir, "values"))

            println(">>> Generated all string resources")
        } catch (e: Exception) {
            println(">>> Error generating strings from twine ==> ${e.message}")
        }
    }
}
