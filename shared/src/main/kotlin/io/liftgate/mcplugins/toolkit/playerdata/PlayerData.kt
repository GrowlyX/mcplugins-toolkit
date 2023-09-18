package io.liftgate.mcplugins.toolkit.playerdata

import java.util.*
import java.util.concurrent.CompletableFuture

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

    fun onInitialCreationAsync(): CompletableFuture<Void>
    {
        return CompletableFuture.completedFuture(null)
    }

    fun onLoadAsync(): CompletableFuture<Void>
    {
        return CompletableFuture.completedFuture(null)
    }

    fun onDestroy(): CompletableFuture<Void>
    {
        return CompletableFuture.completedFuture(null)
    }
}
