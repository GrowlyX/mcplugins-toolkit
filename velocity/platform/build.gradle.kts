dependencies {
    kapt(libs.platform.velocity.api)
    compileOnly(libs.platform.velocity.api)

    api(libs.commands.velocity)
    api(project(":shared"))

    api(kotlin("stdlib"))
    api(project(":velocity:velocity-api"))
}
