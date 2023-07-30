package io.liftgate.mcplugins.toolkit.spigot.playerdata

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.mongodb.client.model.Filters
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.playerdata.PlayerData
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Contract
import org.litote.kmongo.coroutine.CoroutineCollection
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * A lifecycle manager for user data
 * for online players.
 *
 * Data is cached as long as the player
 * is logged onto the server.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
abstract class PlayerDataProvider<T : PlayerData> : Listener, PostConstruct, Eager
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
    fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent)
    {
        runBlocking {
            val playerProfile = collection()
                .findOne(
                    Filters.eq(
                        "_id",
                        event.uniqueId.toString()
                    )
                )
                ?.apply {
                    onLoad()
                }
                ?: createNew(event.uniqueId)
                    .apply {
                        onInitialCreation()
                        onLoad()

                        save(profile = this)
                    }

            playerProfileCache[event.uniqueId] = playerProfile
        }
    }

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent)
    {
        findNullable(event.player)
            ?: return run {
                event.disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    "${ChatColor.RED}Your profile failed to load!"
                )
            }
    }

    @EventHandler
    suspend fun onPlayerJoin(event: PlayerJoinEvent)
    {
        val profile = find(event.player)

        if (event.player.name.isNotEmpty())
        {
            val previousUsername = profile.username
            profile.username = event.player.name

            val usernameNoLongerMatches =
                previousUsername != profile.username

            if (usernameNoLongerMatches)
            {
                save(profile)
                plugin.logger.info(
                    "Pushing username update for ${profile.uniqueId}"
                )
            }
        }
    }

    @EventHandler
    suspend fun onPlayerQuit(event: PlayerQuitEvent)
    {
        val playerProfile = playerProfileCache
            .remove(event.player.uniqueId)
            ?.apply {
                onDestroy()
            }
            ?: return

        save(profile = playerProfile)
    }

    suspend fun save(profile: T)
    {
        withContext(Dispatchers.IO) {
            collection().save(profile)
        }
    }
}
