package io.liftgate.mcplugins.toolkit.spigot.profiles

import java.util.UUID

/**
 * A template for all Minecraft player
 * user data. 
 * 
 * @author GrowlyX
 * @since 6/1/2023
 */
interface PlayerData
{
    val uniqueId: UUID
    var username: String
}
