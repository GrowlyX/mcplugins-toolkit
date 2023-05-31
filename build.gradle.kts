import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.gradle.ext.Gradle

plugins {
    `maven-publish`
    kotlin("jvm") version libs.versions.kotlin
    kotlin("kapt") version libs.versions.kotlin
    kotlin("plugin.serialization") version libs.versions.kotlin
    id("com.github.johnrengelman.shadow") version libs.versions.plugins.shadow
    id("org.jetbrains.gradle.plugin.idea-ext") version libs.versions.plugins.idea.ext
    id("com.google.devtools.ksp") version libs.versions.ksp
}

allprojects {
    group = "io.liftgate.mcplugins"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.gradle.plugin.idea-ext")
    apply(plugin = "com.google.devtools.ksp")
    apply(plugin = "maven-publish")

    dependencies {
        compileOnly(kotlin("stdlib"))
    }

    kotlin {
        jvmToolchain(jdkVersion = 17)
    }

    tasks.withType<ShadowJar> {
        archiveClassifier.set("")
        archiveFileName.set(
            "toolkit-${project.name}.jar"
        )
    }

    sourceSets.main {
        java.srcDirs("build/generated/ksp/main/kotlin")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.javaParameters = true
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }

    publishing {
        publications {
            register(
                name = "mavenJava",
                type = MavenPublication::class,
                configurationAction = shadow::component
            )
        }
    }

    tasks["build"]
        .dependsOn(
            "shadowJar",
            "publishMavenJavaPublicationToMavenLocal"
        )
}

idea {
    project {
        settings {
            runConfigurations {
                create<Gradle>("Build All Modules") {
                    setProject(project)
                    scriptParameters = "clean build"
                }
            }
        }
    }
}
