package com.skoumal.grimoire.twine.tasks

import com.skoumal.grimoire.twine.files.buildFile
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
        val location = when {
            osName.isWindows -> getWindowsBinary()
            else -> getUnixBinary()
        }
        val textLocation = location.joinToString(separator = "\n")

        binary.get().asFile.writeText(textLocation)
    }

    private fun getBinaryArgs(): Array<String> {
        return binary.get().asFile.readText().split('\n').toTypedArray()
    }

    // ---

    private fun getWindowsBinary(): Array<String> {
        return arrayOf("cmd", "/c", "twine")
    }

    private fun getUnixBinary(): Array<String> {
        return arrayOf("twine")
    }

    // ---

    companion object {

        const val name = "findTwineBinary"

        fun register(project: Project) {
            project.tasks.create(name, TwineBinaryTask::class.java) {
                it.binary.set(project.buildFile("twine-binary.rip"))
            }
        }

        fun get(project: Project): Array<String> {
            val task = project.tasks.getByName(name) as TwineBinaryTask
            return task.getBinaryArgs()
        }

    }

}