package io.liftgate.mcplugins.toolkit.spigot.profiles

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.mongodb.client.model.Filters
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import io.liftgate.mcplugins.toolkit.spigot.listeners.CoroutineListener
import jakarta.inject.Inject
import kotlinx.coroutines.withContext
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Contract
import org.litote.kmongo.coroutine.CoroutineCollection
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
abstract class PlayerDataProvider<T : PlayerData> : CoroutineListener, PostConstruct, Eager
{
    private val playerProfileCache =
        ConcurrentHashMap<UUID, T>(1024)

    @Inject
    lateinit var plugin: ToolkitSpigotPlugin

    abstract fun collection(): CoroutineCollection<T>
    abstract fun createNew(uniqueId: UUID): T

    fun findNullable(uniqueId: UUID) = playerProfileCache[uniqueId]
    fun find(uniqueId: UUID) = playerProfileCache[uniqueId]!!

    fun findNullable(player: Player) = playerProfileCache[player.uniqueId]
    fun find(player: Player) = playerProfileCache[player.uniqueId]!!

    override fun postConstruct()
    {
        plugin.server.pluginManager
            .registerSuspendingEvents(
                this, plugin
            )
    }

    @EventHandler
    suspend fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent)
    {
        val playerProfile = collection()
            .findOne(
                Filters.eq(
                    "_id",
                    event.uniqueId.toString()
                )
            )
            ?: createNew(event.uniqueId)

        playerProfileCache[event.uniqueId] = playerProfile
    }

    @EventHandler
    suspend fun onPlayerLogin(event: PlayerLoginEvent)
    {
        val profile = findNullable(event.player)
            ?: return run {
                event.disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    "${ChatColor.RED}Your profile failed to load!"
                )
            }

        val previousUsername = profile.username
        profile.username = event.player.name

        val usernameNoLongerMatches =
            previousUsername != profile.username

        if (usernameNoLongerMatches)
        {
            save(profile)
            plugin.logger.info("Pushing username update for ${profile.uniqueId}")
        }
    }

    @EventHandler
    suspend fun onPlayerQuit(event: PlayerQuitEvent)
    {
        val playerProfile = playerProfileCache
            .remove(event.player.uniqueId)
            ?: return
        save(profile = playerProfile)
    }

    suspend fun save(profile: T)
    {
        withContext(plugin.asyncDispatcher) {
            collection().save(profile)
        }
    }
}
