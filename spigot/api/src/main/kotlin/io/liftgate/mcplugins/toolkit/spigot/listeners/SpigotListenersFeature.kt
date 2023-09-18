package io.liftgate.mcplugins.toolkit.spigot.listeners

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import org.bukkit.event.HandlerList
import org.jvnet.hk2.annotations.Service

/**
 * A core plugin feature eagerly registering suspending
 * events in [ToolkitListener]s.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class SpigotListenersFeature : CorePluginFeature
{
    override fun postEnable(plugin: ToolkitPluginContainer)
    {
        val mcPlugin = plugin.plugin as ToolkitSpigotPlugin

        val listeners = plugin.locator
            .getAllServices<ToolkitListener>()

        listeners.forEach {
            mcPlugin.server.pluginManager
                .registerSuspendingEvents(it, mcPlugin)
        }
    }

    override fun preDisable(plugin: ToolkitPluginContainer)
    {
        val listeners = plugin.locator
            .getAllServices<ToolkitListener>()

        listeners.forEach {
            HandlerList.unregisterAll(it)
        }
    }
}
