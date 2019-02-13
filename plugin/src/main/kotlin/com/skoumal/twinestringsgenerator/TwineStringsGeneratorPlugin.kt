package com.skoumal.twinestringsgenerator

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project

class TwineStringsGeneratorPlugin : Plugin<Project> {

    private lateinit var config: TwineStringsGeneratorPluginExtension
    private var pluginConfigured = false

    override fun apply(project: Project) {

        config = project.extensions.create("twineStringsGenerator", TwineStringsGeneratorPluginExtension::class.java)

        // wait for other plugins added to support applying this before the android plugin
        project.plugins.whenPluginAdded {
            project.afterEvaluate {
                configure(project)
            }
        }
    }

    private fun configure(project: Project) {
        if (pluginConfigured) {
            return
        }

        project.plugins.all {
            val variants: DomainObjectSet<out BaseVariant>? = when (it) {
                is AppPlugin -> project.extensions.getByType(AppExtension::class.java).applicationVariants
                is LibraryPlugin -> project.extensions.getByType(LibraryExtension::class.java).libraryVariants
                else -> null
            }
            if (variants?.isNotEmpty() == true) {
                pluginConfigured = true
                configureAndroid(project, variants)
            }
        }
    }

    private fun <T : BaseVariant> configureAndroid(project: Project, variants: DomainObjectSet<T>) {
        variants.forEach { variant ->
            val outputDirectory = project.file("${project.buildDir}/generated/res/twine/${variant.buildType.name}")
            val twineFile = project.file(config.twineFile)

            val taskName = "generateStringsFor${variant.name.capitalize()}"
            val task = project.tasks.create(taskName, StringsGeneratingTask::class.java).apply {
                sourceFile = twineFile
                outputDir = outputDirectory
                defaultLanguage = config.defaultLanguage
                outputFileName = config.outputFileName
                format = config.format
                args = config.args
            }

            variant.registerGeneratedResFolders(project.files(outputDirectory).builtBy(task))
        }
    }
}
