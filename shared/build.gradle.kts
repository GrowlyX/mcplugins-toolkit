dependencies {
    api(libs.koin.core)
    api(libs.koin.annotations.core)
    ksp(libs.koin.annotations.ksp.compiler)

    api(libs.datastore.lettuce)
    api(libs.datastore.kmongo)
    api(libs.ktx.coroutines.core)
    api(libs.ktx.coroutines.jdk8)
    api(libs.ktx.serialization.json)
    api(libs.ktx.serialization.yaml)
}
