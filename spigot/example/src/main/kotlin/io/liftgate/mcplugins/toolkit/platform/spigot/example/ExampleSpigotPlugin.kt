package io.liftgate.mcplugins.toolkit.platform.spigot.example

import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import org.bukkit.plugin.java.annotation.dependency.Dependency
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Plugin(
    name = "Toolkit-Example",
    version = "1.0.0"
)
@Author("GrowlyX")
@Dependency("Toolkit")
@ApiVersion(ApiVersion.Target.v1_19)
class ExampleSpigotPlugin : ToolkitSpigotPlugin()
{
    override suspend fun enable()
    {

    }
}
