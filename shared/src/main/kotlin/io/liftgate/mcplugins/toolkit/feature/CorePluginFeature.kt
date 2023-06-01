package io.liftgate.mcplugins.toolkit.feature

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.jvnet.hk2.annotations.Contract

/**
 * Generic features which are applied to each plugin.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
interface CorePluginFeature
{
    fun configure(plugin: ToolkitPluginContainer)

    fun bind(
        plugin: ToolkitPluginContainer,
        binder: AbstractBinder.() -> Unit
    )
    {
        ServiceLocatorUtilities
            .bind(
                plugin.locator,
                object : AbstractBinder()
                {
                    override fun configure()
                    {
                        binder(this)
                    }
                }
            )
    }
}
