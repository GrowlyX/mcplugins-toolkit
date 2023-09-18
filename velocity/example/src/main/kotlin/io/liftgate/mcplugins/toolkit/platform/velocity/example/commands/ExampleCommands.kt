package io.liftgate.mcplugins.toolkit.platform.velocity.example.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import com.velocitypowered.api.proxy.Player
import io.liftgate.mcplugins.toolkit.commands.ToolkitCommand
import io.liftgate.mcplugins.toolkit.platform.velocity.example.model.PlayerDataManager
import io.liftgate.mcplugins.toolkit.platform.velocity.component
import jakarta.inject.Inject
import org.jvnet.hk2.annotations.Service
import java.util.concurrent.CompletableFuture.runAsync

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class ExampleCommands : ToolkitCommand()
{
    @Inject
    lateinit var manager: PlayerDataManager

    @CommandAlias("leaderboards|lbs")
    @CommandPermission("op")
    fun onLeaderboards(sender: Player) = runAsync {
        sender.sendMessage("=== Top 10 deaths ===".component())

        manager.aggregateTop10KillsEntries()
            .forEachIndexed { index, result ->
                sender.sendMessage("#${index + 1}. ${result.username}: ${result.value}".component())
            }
    }
}
