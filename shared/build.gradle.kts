plugins {
    id("io.freefair.lombok") version libs.versions.lombok.plugin
    id("org.jetbrains.kotlin.plugin.lombok") version libs.versions.kotlin
}

kapt {
    keepJavacAnnotationProcessors = true
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    compileOnly(libs.lombok.api)
    annotationProcessor(libs.lombok.api)

    compileOnly(libs.commands.core)
    api(libs.cache4k)

    api(libs.datastore.lettuce)
    api(libs.datastore.kmongo)

    api(libs.aware.ktx)

    api(libs.ktx.serialization.json)
    api(libs.ktx.serialization.yaml)
    api(libs.ktx.datetime)

    api(libs.bearlocalizer)
    api(libs.eoyaml)

    api(libs.ktor.client.core)
    api(libs.ktor.client.engine)
    api(libs.ktor.client.serialization)
    api(libs.ktor.client.serialization.core)

    api(libs.hk2.extras)
    api(libs.hk2.locator)
}
