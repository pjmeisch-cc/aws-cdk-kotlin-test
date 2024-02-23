rootProject.name = "aws-cdk-kotlin-test"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            name = "jitpack"
            setUrl("https://jitpack.io")
        }
    }
}

rootDir.listFiles()?.forEach {
    if (it.isDirectory && !it.name.equals("buildSrc", ignoreCase = true) && it.list()?.contains("build.gradle.kts") == true) {
        include(it.name)
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
