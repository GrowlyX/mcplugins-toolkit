dependencies {
    compileOnly(libs.commands.core)
    api(libs.cache4k)

    api(libs.datastore.lettuce)
    api(libs.datastore.kmongo)

    api(libs.ktx.coroutines.core)
    api(libs.ktx.coroutines.jdk8)
    api(libs.ktx.serialization.json)
    api(libs.ktx.serialization.yaml)
    api(libs.ktx.datetime)

    api(libs.ktor.client.core)
    api(libs.ktor.client.engine)
    api(libs.ktor.client.serialization)
    api(libs.ktor.client.serialization.core)

    api(libs.hk2.extras)
    api(libs.hk2.locator)
}
