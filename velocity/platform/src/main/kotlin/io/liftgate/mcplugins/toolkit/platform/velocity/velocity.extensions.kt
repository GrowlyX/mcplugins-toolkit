package io.liftgate.mcplugins.toolkit.platform.velocity

import com.github.shynixn.mccoroutine.velocity.velocityDispatcher
import kotlinx.coroutines.withContext
import net.kyori.adventure.text.Component

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
private fun platform() = ToolkitVelocityPlatformPlugin.plugin

suspend fun <T> runAsync(
    runnable: suspend () -> T
) = withContext(
    platform().pluginContainer.velocityDispatcher
) {
    runnable()
}

fun String.component() = Component.text(this)
