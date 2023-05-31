dependencies {
    api(libs.datastore.lettuce)
    api(libs.datastore.kmongo)
    api(libs.ktx.coroutines.core)
    api(libs.ktx.coroutines.jdk8)
    api(libs.ktx.serialization.json)
    api(libs.ktx.serialization.yaml)
    api(libs.ktx.datetime)

    api(libs.hk2.extras)
    api(libs.hk2.locator)
}
