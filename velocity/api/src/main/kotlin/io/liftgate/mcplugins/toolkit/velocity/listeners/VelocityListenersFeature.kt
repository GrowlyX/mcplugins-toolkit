package io.liftgate.mcplugins.toolkit.velocity.listeners

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin
import org.jvnet.hk2.annotations.Service

/**
 * A core plugin feature eagerly registering suspending
 * events in [ToolkitListener]s.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class VelocityListenersFeature : CorePluginFeature
{
    override fun postEnable(plugin: ToolkitPluginContainer)
    {
        val mcPlugin = plugin.plugin as ToolkitVelocityPlugin

        val listeners = plugin.locator
            .getAllServices<ToolkitListener>()

        listeners.forEach {
            mcPlugin.server.eventManager
                .register(
                    plugin.plugin, it
                )
        }
    }

    override fun preDisable(plugin: ToolkitPluginContainer)
    {
        val mcPlugin = plugin.plugin as ToolkitVelocityPlugin
        val listeners = plugin.locator
            .getAllServices<ToolkitListener>()

        listeners.forEach {
            mcPlugin.server.eventManager
                .unregisterListener(plugin.plugin, it)
        }
    }
}
