package io.liftgate.mcplugins.toolkit.platform.spigot

import io.liftgate.mcplugins.toolkit.ToolkitPlatform
import io.liftgate.mcplugins.toolkit.platform
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
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
class ToolkitSpigotPlatformPlugin : ToolkitSpigotPlugin()
{
    companion object
    {
        @JvmStatic
        lateinit var plugin: ToolkitSpigotPlatformPlugin
    }

    override fun onLoad()
    {
        platform = object : ToolkitPlatform()
        {
            override fun hasPlugin(plugin: String) =
                server.pluginManager.isPluginEnabled(plugin)
        }

        super.onLoad()
    }

    override suspend fun enable()
    {
        plugin = this
    }
}
