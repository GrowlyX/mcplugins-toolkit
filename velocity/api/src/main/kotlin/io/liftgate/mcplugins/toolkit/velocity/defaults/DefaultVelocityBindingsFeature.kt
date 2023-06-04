package io.liftgate.mcplugins.toolkit.velocity.defaults

import com.velocitypowered.api.proxy.ProxyServer
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.bindTo
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.pluginBinder
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin
import org.jvnet.hk2.annotations.Service
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class DefaultVelocityBindingsFeature : CorePluginFeature
{
    override fun rank() = 30

    @Suppress("UNCHECKED_CAST")
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        val mcPlugin = plugin.plugin as ToolkitVelocityPlugin

        pluginBinder(plugin) {
            bind(plugin.plugin as ToolkitVelocityPlugin)
                .bindTo(
                    ToolkitPlugin::class,
                    ToolkitVelocityPlugin::class,
                    plugin.plugin::class as KClass<in ToolkitVelocityPlugin>
                )

            bind(mcPlugin.server)
                .bindTo(ProxyServer::class)
        }
    }
}
