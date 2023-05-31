rootProject.name = "mcplugins-toolkit"

include("shared")
listOf("spigot").forEach {
    include("$it:api", "$it:platform")
}
