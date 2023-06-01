package io.liftgate.mcplugins.toolkit

import kotlinx.coroutines.runBlocking

/**
 * Returns a filtered list of descriptors where the
 * descriptor being filtered has qualifier of [T].
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
