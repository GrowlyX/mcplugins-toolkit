# Toolkit
Kotlin Minecraft plugin development toolkit.

## Features:
 - Service management & DI ([HK2](https://javaee.github.io/hk2/introduction.html)) 
   - Auto-scanning of @Service classes at compile-time
   - Automatic listener registration (implementing CoroutineListener)
   - Automatic command registration (implementing ToolkitCommand)
   - Plugin lifecycles and "features" (using CorePluginFeature)
     - References taken from [PluginInject](https://github.com/natemort/PluginInject)
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

## Platforms:
- Spigot
  - _Tested on:_ **1.19**
  - [Usage Example](https://github.com/GrowlyX/mcplugins-toolkit/tree/master/spigot/example)

### TODO:
 - Yaml configuration (via kaml)
