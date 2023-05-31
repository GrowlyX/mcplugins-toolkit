package io.liftgate.mcplugins.toolkit.platform.spigot

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.withContext

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
fun platform() = ToolkitSpigotPlatformPlugin.plugin

suspend fun runSync(
    runnable: suspend () -> Unit
) = withContext(
    platform().minecraftDispatcher
) {
    runnable()
}

suspend fun runAsync(
    runnable: suspend () -> Unit
) = withContext(
    platform().asyncDispatcher
) {
    runnable()
}
