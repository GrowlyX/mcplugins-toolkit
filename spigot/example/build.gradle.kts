dependencies {
    kapt(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.api)
    compileOnly(libs.aware.ktx)
    compileOnly(project(":spigot:spigot-platform"))
}
