# Toolkit
Kotlin Minecraft plugin development toolkit.

## Features:
 - Service management & DI ([HK2](https://javaee.github.io/hk2/introduction.html)) 
 - Data store (via MongoDB)
   - Player profile caching
 - Coroutine plugin framework (via [MCCoroutine](https://github.com/Shynixn/MCCoroutine))
 - Commands (via [ACF](https://github.com/aikar/commands))
 - Serialization (via [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization))
   - Custom Spigot serializers

## Getting started:
 - Clone this repository
 - Opening the project in your JetBrains IDE:
   - Click run on the pre-defined `Build All Modules` gradle configuration.
 - Building through terminal:
   - Run `./gradlew clean build`
 - Copy the pre-built artifacts from `(module)/build/libs/toolkit-(module).jar`

### Examples:
[Spigot](https://github.com/GrowlyX/mcplugins-toolkit/tree/master/spigot/example)

### TODO:
 - Yaml configuration (via kaml)
