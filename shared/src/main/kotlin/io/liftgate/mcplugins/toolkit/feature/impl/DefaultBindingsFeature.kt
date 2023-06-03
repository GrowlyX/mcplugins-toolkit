package io.liftgate.mcplugins.toolkit.feature.impl

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.bindTo
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import org.glassfish.hk2.api.ServiceLocator
import org.jvnet.hk2.annotations.Service
import java.util.logging.Logger

/**
 * Binds the [ToolkitPluginContainer] into our local ServiceLocator.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class DefaultBindingsFeature : CorePluginFeature
{
    override fun rank() = 30
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        pluginBinder(plugin) {
            bind(plugin).bindTo(ToolkitPluginContainer::class)
            bind(plugin.locator).bindTo(ServiceLocator::class)
            bind(plugin.plugin.getLogger()).bindTo(Logger::class)
        }
    }
}
