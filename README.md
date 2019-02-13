# TwineStringsGeneratorPlugin #

Gradle plugin for generating (android) strings from [twine](https://github.com/scelis/twine) file.

### Installation ###

[![](https://jitpack.io/v/skoumalcz/TwineStringsGeneratorPlugin.svg)](https://jitpack.io/#skoumalcz/TwineStringsGeneratorPlugin)

Project root build.gradle
```groovy
buildscript {
    repositories {
        //...
        maven { url "https://jitpack.io" }
    }

    dependencies {
        //...
        classpath "com.github.skoumalcz:TwineStringsGeneratorPlugin:latest.version"
    }
}
```

Module build.gradle
```groovy
apply plugin: 'com.skoumal.twinestringsgenerator.plugin'

twineStringsGenerator {
    //path to your twine file, mandatory
    twineFile = "strings.txt"
    //name of generated string file(s), defaults to "strings.xml"
    outputFileName = "strings.xml"
    //default language which will be placed to values directory, defaults to "en"
    defaultLanguage = "en"
    //format of generated files, defaults to "android"
    format = "android"
    //additional arguments you want to pass to twine executable, defaults to empty list
    args = ["-itranslated", "-u", "-tandroid"]
}
```
