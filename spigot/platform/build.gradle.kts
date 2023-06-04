dependencies {
    kapt(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.annotate)
    compileOnly(libs.platform.spigot.api)

    api(libs.commands.bukkit)

    api(libs.mccoroutine.bukkit.api)
    api(libs.mccoroutine.bukkit.core)
    api(project(":shared"))

    api(kotlin("stdlib"))
    api(project(":spigot:spigot-api"))
}
