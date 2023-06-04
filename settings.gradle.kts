rootProject.name = "mcplugins-toolkit"

include("shared")
listOf("spigot", "velocity").forEach {
    include("$it:api", "$it:platform", "$it:example")
}
