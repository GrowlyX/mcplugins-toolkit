dependencies {
    compileOnly(libs.platform.spigot.api)
    api(libs.mccoroutine.bukkit.api)
    api(libs.mccoroutine.bukkit.core)
    ksp(libs.koin.annotations.ksp.compiler)

    api(project(":shared"))
}
