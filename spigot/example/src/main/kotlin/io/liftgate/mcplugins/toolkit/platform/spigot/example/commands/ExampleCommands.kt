package io.liftgate.mcplugins.toolkit.platform.spigot.example.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import io.liftgate.mcplugins.toolkit.commands.ToolkitCommand
import io.liftgate.mcplugins.toolkit.platform.spigot.example.model.PlayerDataManager
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Horse
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class ExampleCommands : ToolkitCommand()
{
    @Inject
    lateinit var manager: PlayerDataManager

    @Inject
    lateinit var json: Json

    @CommandAlias("leaderboards|lbs")
    @CommandPermission("op")
    fun onLeaderboards(sender: CommandSender) =
        runBlocking {
            sender.sendMessage("=== Top 10 deaths ===")

            manager.aggregateTop10KillsEntries()
                .forEachIndexed { index, result ->
                    sender.sendMessage("${ChatColor.BOLD}#${index + 1}. ${ChatColor.RESET}${result.username}: ${result.value}")
                }

            sender.sendMessage("[runBlocking] on ${Thread.currentThread().name}")
        }

    @CommandAlias("test-serialization")
    @CommandPermission("op")
    fun onTestSerialization(sender: ConsoleCommandSender)
    {
        val stack = ItemStack(Material.SUGAR)
        sender.sendMessage(
            json.encodeToString(stack)
        )

        val color = Color.AQUA
        sender.sendMessage("===")
        sender.sendMessage(
            json.encodeToString(color)
        )

        val potion = PotionEffect(
            PotionEffectType.FIRE_RESISTANCE, 10000, 1
        )
        sender.sendMessage("===")
        sender.sendMessage(
            json.encodeToString(potion)
        )
    }
}
