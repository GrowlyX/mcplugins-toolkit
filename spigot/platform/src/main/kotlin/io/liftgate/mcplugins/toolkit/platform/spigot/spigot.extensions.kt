package io.liftgate.mcplugins.toolkit.platform.spigot

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.withContext

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
fun platform() = ToolkitSpigotPlatformSpigotPlugin.plugin

suspend fun <T> runSync(
    runnable: suspend () -> T
) = withContext(
    platform().minecraftDispatcher
) {
    runnable()
}

suspend fun <T> runAsync(
    runnable: suspend () -> T
) = withContext(
    platform().asyncDispatcher
) {
    runnable()
}
