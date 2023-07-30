package io.liftgate.mcplugins.toolkit.playerdata

import java.util.*

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

    suspend fun onInitialCreation()
    {

    }

    suspend fun onLoad()
    {

    }

    suspend fun onDestroy()
    {

    }
}
