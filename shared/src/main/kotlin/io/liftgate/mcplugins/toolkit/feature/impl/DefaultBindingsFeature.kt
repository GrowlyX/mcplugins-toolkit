package io.liftgate.mcplugins.toolkit.feature.impl

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.hk2.BindingBuilderUtilities
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
        bind(plugin) {
            bind(plugin)
                .apply {
                    BindingBuilderUtilities
                        .bindTo(
                            this,
                            ToolkitPluginContainer::class.java
                        )
                }

            bind(plugin.locator)
                .apply {
                    BindingBuilderUtilities
                        .bindTo(
                            this,
                            ServiceLocator::class.java
                        )
                }

            bind(plugin.plugin.getLogger())
                .apply {
                    BindingBuilderUtilities
                        .bindTo(
                            this,
                            Logger::class.java
                        )
                }
        }
    }
}
