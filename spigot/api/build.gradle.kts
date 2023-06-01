dependencies {
    compileOnly(libs.platform.spigot.api)
    compileOnly(libs.mccoroutine.bukkit.api)
    compileOnly(libs.mccoroutine.bukkit.core)
    compileOnly(libs.commands.bukkit)

    compileOnly(project(":shared"))
}
