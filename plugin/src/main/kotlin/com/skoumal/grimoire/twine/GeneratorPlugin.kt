package com.skoumal.grimoire.twine

import com.android.build.gradle.BaseExtension
import com.skoumal.grimoire.twine.extensions.TwineExtension
import com.skoumal.grimoire.twine.files.generatedResDir
import com.skoumal.grimoire.twine.tasks.TwineBinaryTask
import com.skoumal.grimoire.twine.tasks.TwineGenerateTask
import com.skoumal.grimoire.twine.tasks.TwineRenameTask
import com.skoumal.grimoire.twine.tasks.TwineTask
import com.skoumal.grimoire.twine.tool.debug
import com.skoumal.grimoire.twine.tool.error
import org.gradle.api.Plugin
import org.gradle.api.Project

class GeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = registerExtension(target)
        target.afterEvaluate {
            registerTasks(it, extension)
        }
    }

    private fun registerExtension(project: Project): TwineExtension {
        return TwineExtension.create(project)
    }

    private fun getBaseExtension(project: Project): BaseExtension? {
        return project.extensions.findByType(BaseExtension::class.java)
    }

    private fun registerTasks(target: Project, extension: TwineExtension) {
        val baseExtension = getBaseExtension(target)
        if (baseExtension == null) {
            target.logger.error { "Project ${target.name} doesn't contain supported Android plugins." }
            return
        }

        val outputDir = target.generatedResDir()
        baseExtension.sourceSets.forEach {
            target.logger.debug { "Added directory ${outputDir.absoluteFile} to res sources" }
            it.res.srcDir(outputDir)
        }

        target.logger.debug { TwineExtension.toString(extension) }

        TwineBinaryTask.register(target)
        TwineGenerateTask.register(target, extension, outputDir)
        TwineRenameTask.register(target, extension, outputDir)
        TwineTask.register(target)
    }

}