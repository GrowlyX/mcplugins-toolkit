package io.liftgate.mcplugins.toolkit.spigot.commands

import co.aikar.commands.PaperCommandManager
import org.jvnet.hk2.annotations.Contract

/**
 * Customizes a plugin-specific [PaperCommandManager] 
 * prior to command registration.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
interface SpigotCommandManagerCustomizer
{
    fun customize(manager: PaperCommandManager)
}
