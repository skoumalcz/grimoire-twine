package com.skoumal.grimoire.twine.tasks

import com.skoumal.grimoire.twine.extensions.TwineExtension
import com.skoumal.grimoire.twine.tool.debug
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class TwineRenameTask : DefaultTask() {

    @get:Input
    abstract val language: Property<String>

    @get:InputDirectory
    abstract val input: RegularFileProperty

    @TaskAction
    fun onRename() {
        val language = language.get()
        val input = input.get().asFile
        val target = File(input, "values-$language")
        val result = File(input, "values")

        project.logger.debug {
            if (target.exists()) "Renaming from \"values-$language\" to \"values\""
            else "Renaming not necessary, \"values-$language\" doesn't exist"
        }

        target.renameTo(result)
    }

    companion object {

        const val name = "renameTwineResources"

        fun register(
            project: Project,
            extension: TwineExtension,
            output: File
        ) {
            val klass = TwineRenameTask::class.java
            project.tasks.create(name, klass) {
                it.dependsOn(TwineGenerateTask.name)
                it.group = "twine"

                it.language.set(extension.defaultLanguage.getOrElse("en"))
                it.input.set(output)
            }
        }

    }

}