package com.skoumal.grimoire.twine.tasks

import com.skoumal.grimoire.twine.files.buildFile
import com.skoumal.grimoire.twine.tool.debug
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class TwineBinaryTask : DefaultTask() {

    @get:OutputFile
    abstract val binary: RegularFileProperty

    private val String.isWindows
        get() = contains("windows", ignoreCase = true)

    @TaskAction
    fun onFindBinary() {
        val osName = System.getProperties()["os.name"].toString()
        project.logger.debug { "OS name is \"$osName\"" }
        val location = when {
            osName.isWindows -> getWindowsBinary()
            else -> getUnixBinary()
        }
        val textLocation = location.joinToString(separator = System.lineSeparator())

        if (project.logger.isDebugEnabled) {
            val inlineLocation = textLocation.replace(System.lineSeparator(), " ")
            project.logger.debug("Writing \"$inlineLocation\" for OS \"$osName\"")
        }

        binary.get().asFile.writeText(textLocation)
    }

    private fun getBinaryArgs(): Array<String> {
        project.logger.debug { "Reading cached binary location" }
        return binary.get().asFile.readText().split(System.lineSeparator()).toTypedArray()
    }

    // ---

    private fun getWindowsBinary(): Array<String> {
        project.logger.debug { "Found Windows configuration" }
        return arrayOf("cmd", "/c", "twine")
    }

    private fun getUnixBinary(): Array<String> {
        project.logger.debug { "Found Unix configuration" }
        return arrayOf("twine")
    }

    // ---

    companion object {

        const val name = "findTwineBinary"

        fun register(project: Project) {
            project.tasks.create(name, TwineBinaryTask::class.java) {
                it.group = "twine"
                it.binary.set(project.buildFile("twine-binary.rip"))
            }
        }

        fun get(project: Project): Array<String> {
            val task = project.tasks.getByName(name) as TwineBinaryTask
            return task.getBinaryArgs()
        }

    }

}