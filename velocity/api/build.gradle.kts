dependencies {
    compileOnly(libs.platform.velocity.api)
    compileOnly(libs.mccoroutine.velocity.api)
    compileOnly(libs.mccoroutine.velocity.core)
    compileOnly(libs.commands.velocity)

    compileOnly(project(":shared"))
}
