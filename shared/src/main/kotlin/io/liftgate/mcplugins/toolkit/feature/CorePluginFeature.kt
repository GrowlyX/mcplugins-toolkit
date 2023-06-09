package io.liftgate.mcplugins.toolkit.feature

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.jvnet.hk2.annotations.Contract

/**
 * Generic lifecycle events called for each [ToolkitPlugin]. Core plugin
 * features share functionality similar to services marked with @Export.
 * Unlike exported services, [CorePluginFeature] events are called on the local plugin as well.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
interface CorePluginFeature
{
    fun rank() = 0

    fun preEnable(plugin: ToolkitPluginContainer)
    {

    }

    fun postEnable(plugin: ToolkitPluginContainer)
    {

    }

    fun preDisable(plugin: ToolkitPluginContainer)
    {

    }
}
