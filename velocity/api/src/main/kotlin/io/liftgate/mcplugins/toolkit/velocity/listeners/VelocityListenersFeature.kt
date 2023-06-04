package io.liftgate.mcplugins.toolkit.velocity.listeners

import com.github.shynixn.mccoroutine.velocity.registerSuspend
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin
import org.jvnet.hk2.annotations.Service

/**
 * A core plugin feature eagerly registering suspending
 * events in [CoroutineListener]s.
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
            .getAllServices<CoroutineListener>()

        listeners.forEach {
            mcPlugin.server.eventManager
                .registerSuspend(
                    plugin.plugin, it
                )
        }
    }

    override fun preDisable(plugin: ToolkitPluginContainer)
    {
        val mcPlugin = plugin.plugin as ToolkitVelocityPlugin
        val listeners = plugin.locator
            .getAllServices<CoroutineListener>()

        listeners.forEach {
            mcPlugin.server.eventManager
                .unregisterListener(plugin.plugin, it)
        }
    }
}
