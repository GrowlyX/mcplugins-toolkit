package io.liftgate.mcplugins.toolkit.velocity.commands.manager

import co.aikar.commands.VelocityCommandManager
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin

/**
 * @author GrowlyX
 * @since 6/3/2023
 */
class ToolkitCommandManager(
    plugin: ToolkitVelocityPlugin
) : VelocityCommandManager(plugin.server, plugin)
{
    fun registerDefaults()
    {
        enableUnstableAPI("help")
    }
}
