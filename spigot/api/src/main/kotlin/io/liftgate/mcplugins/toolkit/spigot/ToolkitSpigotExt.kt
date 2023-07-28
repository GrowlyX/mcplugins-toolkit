package io.liftgate.mcplugins.toolkit.spigot

import io.liftgate.localize.identity.Identity
import io.liftgate.localize.identity.IdentityService
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/28/2023
 */
val Player.identity: Identity
    get() = IdentityService
        .identities()
        .identity(uniqueId)
