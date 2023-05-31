dependencies {
    kapt(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.api)
    ksp(libs.koin.annotations.ksp.compiler)

    api(kotlin("stdlib"))
    api(project(":api:bukkit"))
}
