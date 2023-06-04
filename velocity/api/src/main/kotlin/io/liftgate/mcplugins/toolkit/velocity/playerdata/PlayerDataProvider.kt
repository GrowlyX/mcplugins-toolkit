package io.liftgate.mcplugins.toolkit.velocity.playerdata

import com.github.shynixn.mccoroutine.velocity.registerSuspend
import com.mongodb.client.model.Filters
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.proxy.Player
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.playerdata.PlayerData
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin
import io.liftgate.mcplugins.toolkit.velocity.listeners.CoroutineListener
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
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
abstract class PlayerDataProvider<T : PlayerData> : CoroutineListener, PostConstruct, Eager
{
    private val playerProfileCache =
        ConcurrentHashMap<UUID, T>(1024)

    @Inject
    lateinit var plugin: ToolkitVelocityPlugin

    private var readOnlyCopies = false

    abstract fun collection(): CoroutineCollection<T>
    abstract fun createNew(uniqueId: UUID): T

    fun findNullable(uniqueId: UUID) = playerProfileCache[uniqueId]
    fun find(uniqueId: UUID) = playerProfileCache[uniqueId]!!

    fun findNullable(player: Player) = playerProfileCache[player.uniqueId]
    fun find(player: Player) = playerProfileCache[player.uniqueId]!!

    fun setReadOnly()
    {
        readOnlyCopies = true
    }

    override fun postConstruct()
    {
        plugin.server.eventManager
            .registerSuspend(
                plugin, this
            )
    }

    @Subscribe
    suspend fun onPlayerPreLogin(event: LoginEvent)
    {
        runCatching {
            val playerProfile = collection()
                .findOne(
                    Filters.eq(
                        "_id",
                        event.player.uniqueId.toString()
                    )
                )
                ?: createNew(event.player.uniqueId)

            playerProfileCache[event.player.uniqueId] = playerProfile

            val previousUsername = playerProfile.username
            playerProfile.username = event.player.username

            val usernameNoLongerMatches =
                previousUsername != playerProfile.username

            if (usernameNoLongerMatches)
            {
                save(playerProfile)
                plugin._logger.info(
                    "Pushing username update for ${playerProfile.uniqueId}"
                )
            }
        }.onFailure {
            event.result = ResultedEvent.ComponentResult.denied(
                Component
                    .text("Your profile failed to load!")
                    .color(NamedTextColor.RED)
            )
        }
    }

    @Subscribe
    suspend fun onPlayerQuit(event: DisconnectEvent)
    {
        val playerProfile = playerProfileCache
            .remove(event.player.uniqueId)
            ?: return
        save(profile = playerProfile)
    }

    suspend fun save(profile: T)
    {
        if (readOnlyCopies)
        {
            return
        }

        withContext(Dispatchers.IO) {
            collection().save(profile)
        }
    }
}
