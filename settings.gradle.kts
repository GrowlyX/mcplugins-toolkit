rootProject.name = "mcplugins-toolkit"

include("shared")
listOf("spigot", "velocity").forEach {
    listOf("$it:api", "$it:platform", "$it:example")
        .forEach { module ->
            include(module)

            val compat = module.replace(":", "-")
            project(":$module").name = compat
        }
}
