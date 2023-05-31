package io.liftgate.mcplugins.toolkit.platform.spigot

import io.liftgate.mcplugins.toolkit.spigot.ToolkitPlugin
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Plugin(
    name = "Toolkit",
    version = "1.0.0"
)
@Author("GrowlyX")
@ApiVersion(ApiVersion.Target.v1_19)
class ToolkitSpigotPlatformPlugin : ToolkitPlugin()
{
    companion object
    {
        @JvmStatic
        lateinit var plugin: ToolkitSpigotPlatformPlugin
    }

    override suspend fun enable()
    {
        plugin = this
    }
}
