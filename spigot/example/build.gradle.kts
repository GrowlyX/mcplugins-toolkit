dependencies {
    kapt(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.api)
    compileOnly(project(":spigot:spigot-platform"))
}
