package io.liftgate.mcplugins.toolkit.spigot.listeners

import org.bukkit.event.Listener
import org.jvnet.hk2.annotations.Contract

/**
 * A contract marking eagerly loaded event handlers.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
interface ToolkitListener : Listener
