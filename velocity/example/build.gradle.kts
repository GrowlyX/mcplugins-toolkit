dependencies {
    kapt(libs.platform.velocity.api)
    compileOnly(libs.platform.velocity.api)
    compileOnly(project(":velocity:velocity-platform"))
}
