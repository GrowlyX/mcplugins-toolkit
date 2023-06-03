package io.liftgate.mcplugins.toolkit.platform.spigot.listener

import io.liftgate.mcplugins.toolkit.platform.spigot.runAsync
import io.liftgate.mcplugins.toolkit.profile.StoredPlayerProfile
import io.liftgate.mcplugins.toolkit.profile.StoredPlayerProfileManager
import io.liftgate.mcplugins.toolkit.spigot.listeners.CoroutineListener
import jakarta.inject.Inject
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/3/2023
 */
@Service
class StoredPlayerProfileListener : CoroutineListener
{
    @Inject
    lateinit var profileManager: StoredPlayerProfileManager

    @EventHandler
    suspend fun onPlayerJoin(event: PlayerJoinEvent)
    {
        runAsync {
            val profile = StoredPlayerProfile(
                uniqueId = event.player.uniqueId,
                username = event.player.name
            )

            profileManager
                .cacheStoredProfile(
                    storedProfile = profile
                )

            profileManager
                .findDuplicates(
                    profile.username, profile.uniqueId
                )
                .forEach {
                    val mojangProfile = profileManager
                        .loadAPIProfileFromUniqueId(
                            it.uniqueId
                        )
                        ?: return@forEach

                    it.username = mojangProfile.username
                    profileManager.cacheStoredProfile(it)
                }
        }
    }
}
