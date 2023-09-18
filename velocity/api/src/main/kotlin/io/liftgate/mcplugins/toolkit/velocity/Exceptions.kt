package io.liftgate.mcplugins.toolkit.velocity

import io.liftgate.mcplugins.toolkit.ToolkitPlugin

/**
 * @author GrowlyX
 * @since 6/4/2023
 */
class HK2FailedToLoadDescriptorsException(plugin: ToolkitPlugin) : Exception(
    "HK2 failed to load descriptors for plugin ${plugin.getName()}"
)

class PluginFailedToEnableException(plugin: ToolkitPlugin) : Exception(
    "Toolkit failed to load plugin ${plugin.getName()}"
)
