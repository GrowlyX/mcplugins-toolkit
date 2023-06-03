package io.liftgate.mcplugins.toolkit.platform.spigot.example.commands

import co.aikar.commands.annotation.CommandAlias
import io.liftgate.mcplugins.toolkit.commands.ToolkitCommand
import io.liftgate.mcplugins.toolkit.platform.spigot.example.model.PlayerDataManager
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class LeaderboardCommand : ToolkitCommand()
{
    @Inject
    lateinit var manager: PlayerDataManager

    @CommandAlias("leaderboards|lbs")
    fun onLeaderboards(sender: CommandSender) =
        runBlocking {
            sender.sendMessage("=== Top 10 deaths ===")

            manager.aggregateTop10KillsEntries()
                .forEachIndexed { index, result ->
                    sender.sendMessage("${ChatColor.BOLD}#${index + 1}. ${ChatColor.RESET}${result.username}: ${result.value}")
                }

            sender.sendMessage("[runBlocking] on ${Thread.currentThread().name}")
        }
}
