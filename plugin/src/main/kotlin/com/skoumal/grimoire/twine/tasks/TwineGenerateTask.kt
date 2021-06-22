package com.skoumal.grimoire.twine.tasks

import com.skoumal.grimoire.twine.extensions.TwineExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class TwineGenerateTask : DefaultTask() {

    // >>- inputs

    @get:Input
    abstract val fileName: Property<String>

    @get:Input
    abstract val format: Property<String>

    @get:Input
    abstract val args: ListProperty<String>

    @get:InputFile
    abstract val source: RegularFileProperty

    // >>- outputs

    @get:OutputDirectory
    abstract val output: RegularFileProperty

    // ---

    @TaskAction
    fun onRunTask() {
        val twine = getTwine()
        if (logger.isDebugEnabled) {
            logger.debug("Twine command: \"${twine.joinToString(separator = " ")}\"")
        }

        val command = compileCommand()
        if (logger.isDebugEnabled) {
            logger.debug("Twine command args: ${command.joinToString(separator = " ")}")
        }

        val result = project.exec {
            it.commandLine(*twine, *command)
        }

        // requires this process to always complete successfully
        // if it doesn't complete correctly, then the build is worthless anyway
        result.rethrowFailure()
    }

    // ---

    private fun getTwine(): Array<String> {
        return TwineBinaryTask.get(project)
    }

    private fun compileCommand(): Array<String> {
        val core = arrayOf(
            "generate-all-localization-files",
            "-n${fileName.get()}",
            "-f${format.get()}",
            "-r"
        )
        val args = args.get()
        val files = arrayOf(
            source.get().asFile.absolutePath,
            output.get().asFile.absolutePath
        )

        return core + args + files
    }

    // ---

    companion object {

        const val name = "generateTwineResources"

        fun register(
            project: Project,
            extension: TwineExtension,
            output: File
        ) {
            val klass = TwineGenerateTask::class.java
            val task = project.tasks.findByName(name)

            if (task != null) {
                task as TwineGenerateTask
            } else {
                project.tasks.create(name, klass)
            }.also {
                it.dependsOn(TwineBinaryTask.name)

                it.fileName.set(extension.outputName.getOrElse("values.xml"))
                it.format.set(extension.format.getOrElse("android"))
                it.args.set(extension.args.getOrElse(emptyList()))
                it.source.set(project.file(extension.file))
                it.output.set(output)
            }
        }

    }

}