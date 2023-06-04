package io.liftgate.mcplugins.toolkit.platform.velocity.listener

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import io.liftgate.mcplugins.toolkit.profile.StoredPlayerProfile
import io.liftgate.mcplugins.toolkit.profile.StoredPlayerProfileManager
import io.liftgate.mcplugins.toolkit.velocity.listeners.CoroutineListener
import jakarta.inject.Inject
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

    @Subscribe(order = PostOrder.LATE)
    suspend fun onPlayerJoin(event: LoginEvent)
    {
        val profile = StoredPlayerProfile(
            uniqueId = event.player.uniqueId,
            username = event.player.username
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
