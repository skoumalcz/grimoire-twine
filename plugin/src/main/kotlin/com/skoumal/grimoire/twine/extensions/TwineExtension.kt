package com.skoumal.grimoire.twine.extensions

import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface TwineExtension {

    /**
     * Output file name.
     *
     * Defaults to `values.xml`. Always include the suffix ".xml"
     * */
    val outputName: Property<String>

    /**
     * Specifies default language. This is used to fix resource directory generation, as twine
     * would often create folders named `values-en`, which obviously causes the build to fail as
     * there's no default res directory.
     *
     * Defaults to `en`
     * */
    val defaultLanguage: Property<String>

    /**
     * Specifies output twine format. Probably don't change this ever, thanks.
     *
     * Defaults to `android`.
     * */
    val format: Property<String>

    /**
     * Specifies the input file for twine file. Twine files are plaintext. Suffix is irrelevant.
     * Include the whole file name, possibly with entire path to the file if it's not in the scope
     * of this project.
     * */
    val file: Property<String>

    /**
     * Specifies additional arguments which will be unconditionally passed to the twine command.
     *
     * Defaults to empty set.
     * */
    val args: ListProperty<String>

    companion object {

        fun create(project: Project): TwineExtension {
            return project.extensions.create("twine", TwineExtension::class.java)
        }

    }

}