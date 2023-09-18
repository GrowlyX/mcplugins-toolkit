package io.liftgate.mcplugins.toolkit.platform.velocity

import net.kyori.adventure.text.Component

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
private fun platform() = ToolkitVelocityPlatformPlugin.plugin
fun String.component() = Component.text(this)
