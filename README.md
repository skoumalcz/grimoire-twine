<p align="center">
  <img src="art/logo.svg" width="128px" />
</p>
<h1 align="center">Grimoire<sup>Twine</sup></h1>

Gradle plugin for generating (android) strings from [twine](https://github.com/scelis/twine) file.

### Installation ###

Project root build.gradle

```groovy
buildscript {
    dependencies {
        //...
        classpath "com.skoumal.grimoire:twine:latest.version"
    }
}
```

Module build.gradle

```groovy
apply plugin: 'grimoire.twine'

twine {
    outputName = "strings.xml" // optional
    defaultLanguage = "en" // optional
    format = "android" // optional
    file = "${rootDir}/strings.txt"
    args = ["-itranslated", "-u", "-tandroid"] // optional
}
```

Logo by <a href="https://www.flaticon.com/authors/smalllikeart" title="smalllikeart">
smalllikeart</a>