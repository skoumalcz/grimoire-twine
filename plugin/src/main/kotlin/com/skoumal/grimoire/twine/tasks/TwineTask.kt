package com.skoumal.grimoire.twine.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.Project

abstract class TwineTask : DefaultTask() {

    companion object {

        const val name = "assembleTwineResources"

        fun register(project: Project) {
            val task = project.tasks.create(name, TwineTask::class.java) {
                it.dependsOn(TwineRenameTask.name)
                it.group = "twine"
            }

            project.tasks.findByName("preBuild")?.dependsOn(task)
        }

    }

}