package io.liftgate.mcplugins.toolkit.platform.spigot.listener

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import io.liftgate.mcplugins.toolkit.configuration.Configuration
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.platform.spigot.profile.ProfileCachingConfig
import io.liftgate.mcplugins.toolkit.platform.spigot.runAsync
import io.liftgate.mcplugins.toolkit.profile.StoredPlayerProfile
import io.liftgate.mcplugins.toolkit.profile.StoredPlayerProfileManager
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import jakarta.inject.Inject
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/3/2023
 */
@Service
class StoredPlayerProfileListener : Eager, PostConstruct, Listener
{
    @Inject
    lateinit var profileManager: StoredPlayerProfileManager

    @Inject
    lateinit var plugin: ToolkitSpigotPlugin

    @Inject
    lateinit var configuration: Configuration<ProfileCachingConfig.Model>

    @EventHandler
    suspend fun onPlayerJoin(event: PlayerJoinEvent)
    {
        val profile = StoredPlayerProfile(
            uniqueId = event.player.uniqueId,
            username = event.player.name
        )

        profileManager
            .cacheStoredProfile(
                storedProfile = profile
            )

        runAsync {
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

    override fun postConstruct()
    {
        val cachingConfig = configuration.instance()
        if (cachingConfig.enabled)
        {
            Bukkit.getPluginManager()
                .registerSuspendingEvents(
                    this, plugin
                )
        }
    }
}
