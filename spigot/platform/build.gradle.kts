dependencies {
    kapt(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.api)

    api(libs.commands.bukkit)
    api(project(":shared"))

    api(kotlin("stdlib"))
    api(project(":spigot:spigot-api"))
}
