package io.liftgate.mcplugins.toolkit.platform.spigot.example.listener

import io.liftgate.localize.Localizer
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.redis.RedisDatastore
import io.liftgate.mcplugins.toolkit.message
import io.liftgate.mcplugins.toolkit.platform.spigot.example.localizer.CoreLang
import io.liftgate.mcplugins.toolkit.profile.StoredPlayerProfileManager
import io.liftgate.mcplugins.toolkit.spigot.listeners.CoroutineListener
import jakarta.inject.Inject
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service
import java.util.*

/**
 * @author GrowlyX
 * @since 7/29/2023
 */
@Service
class StaffLoginListener : CoroutineListener, Eager, PostConstruct
{
    @Inject
    lateinit var redis: RedisDatastore

    private val connection by lazy {
        redis.client()
            .buildNewMessageConnection("staff")
    }

    @Inject
    lateinit var profileManager: StoredPlayerProfileManager

    override fun postConstruct()
    {
        connection.connect()
        connection.listen("login") {
            val profile = profileManager
                .loadProfileFromUniqueId(
                    get<UUID>("player")
                )

            Localizer.of<CoreLang>()
                .staffLogin(profile.username)
                .forEach(Bukkit::broadcastMessage)
        }
    }

    @EventHandler
    suspend fun PlayerJoinEvent.on()
    {
        connection
            .message("login")
            .set("player", player.uniqueId)
            .publish()
    }
}
