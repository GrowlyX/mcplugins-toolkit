package io.liftgate.mcplugins.toolkit.feature.impl

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
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
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        bind(plugin) {
            bind(plugin)
                .to(ToolkitPluginContainer::class.java)
            bind(plugin.plugin.getLogger())
                .to(Logger::class.java)
        }
    }
}
