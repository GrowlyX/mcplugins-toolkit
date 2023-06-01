package io.liftgate.mcplugins.toolkit.spigot.defaults

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class DefaultSpigotBindingsFeature : CorePluginFeature
{
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        bind(plugin) {
            bind(plugin.plugin)
                .apply {
                    to(JavaPlugin::class.java)
                    to(SuspendingJavaPlugin::class.java)
                    to(ToolkitPlugin::class.java)
                    to(plugin.plugin.javaClass)
                }
        }
    }
}
