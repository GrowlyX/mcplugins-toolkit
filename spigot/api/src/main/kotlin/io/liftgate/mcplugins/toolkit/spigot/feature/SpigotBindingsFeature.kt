package io.liftgate.mcplugins.toolkit.spigot.feature

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class SpigotBindingsFeature : CorePluginFeature
{
    override fun configure(plugin: ToolkitPluginContainer)
    {
        bind(plugin) {
            bind(plugin.plugin)
                .to(plugin.plugin.javaClass)
        }
    }
}
