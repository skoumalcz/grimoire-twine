package com.skoumal.twinestringsgenerator

fun org.gradle.api.Project.twine(configure: com.skoumal.twinestringsgenerator.TwineStringsGeneratorPluginExtension.() -> Unit): Unit =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("twineStringsGenerator", configure)