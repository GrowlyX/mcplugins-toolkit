package io.liftgate.mcplugins.toolkit.spigot.commands.manager

import co.aikar.commands.ConditionFailedException
import co.aikar.commands.PaperCommandManager
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import org.bukkit.command.ConsoleCommandSender

/**
 * @author GrowlyX
 * @since 6/3/2023
 */
class ToolkitCommandManager(
    plugin: ToolkitSpigotPlugin
) : PaperCommandManager(plugin)
{
    fun registerDefaults()
    {
        enableUnstableAPI("help")
        commandContexts
            .registerIssuerOnlyContext(
                ConsoleCommandSender::class.java
            ) { context ->
                if (context.sender !is ConsoleCommandSender)
                {
                    throw ConditionFailedException(
                        "Only console may perform this command."
                    )
                }

                return@registerIssuerOnlyContext context.sender as ConsoleCommandSender
            }
    }
}
