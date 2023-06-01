package io.liftgate.mcplugins.toolkit

import kotlinx.coroutines.runBlocking

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
inline fun <reified T> ToolkitPluginContainer.getDescriptors() =
    locator
        .getDescriptors {
            it.qualifiers
                .contains(
                    T::class.java.name
                )
        }

fun runBlockingUnsafe(
    exceptionCaught: (Throwable) -> Unit,
    lambda: suspend () -> Unit
) =
    runBlocking {
        val enable = runCatching {
            lambda()
        }.onFailure {
            exceptionCaught(it)
        }

        enable.isFailure
    }
