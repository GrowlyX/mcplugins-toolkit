package io.liftgate.mcplugins.toolkit

import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder

/**
 * @author GrowlyX
 * @since 6/3/2023
 */
lateinit var platform: ToolkitPlatform

abstract class ToolkitPlatform
{
    abstract fun hasPlugin(plugin: String): Boolean
}

fun pluginBinder(
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
