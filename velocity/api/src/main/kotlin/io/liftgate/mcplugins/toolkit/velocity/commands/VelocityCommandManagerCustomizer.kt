package io.liftgate.mcplugins.toolkit.velocity.commands

import co.aikar.commands.VelocityCommandManager
import io.liftgate.mcplugins.toolkit.velocity.commands.manager.ToolkitCommandManager
import org.jvnet.hk2.annotations.Contract

/**
 * Customizes a plugin-specific [VelocityCommandManager]
 * prior to command registration.
 *
 * @author GrowlyX
 * @since 6/4/2023
 */
@Contract
interface VelocityCommandManagerCustomizer
{
    fun customize(manager: ToolkitCommandManager)
}
