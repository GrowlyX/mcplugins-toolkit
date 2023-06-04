package io.liftgate.mcplugins.toolkit

import io.liftgate.mcplugins.toolkit.hk2.BindingBuilderUtilities
import kotlinx.coroutines.runBlocking
import org.glassfish.hk2.utilities.binding.ScopedBindingBuilder
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder
import kotlin.reflect.KClass

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

        enable.isSuccess
    }

fun <T : Any> ScopedBindingBuilder<T>.bindTo(
    vararg kClasses: KClass<in T>
) = apply {
    BindingBuilderUtilities
        .bindTo(
            this,
            *kClasses
                .map { it.java }
                .toTypedArray()
        )
}

fun <T : Any> ServiceBindingBuilder<T>.bindToSuper(
    vararg kClasses: KClass<in T>
) = apply {
    BindingBuilderUtilities
        .bindToSuperService(
            this,
            *kClasses
                .map { it.java }
                .toTypedArray()
        )
}

fun <T : Any> ServiceBindingBuilder<T>.bindTo(
    vararg kClasses: KClass<out T>
) = apply {
    BindingBuilderUtilities
        .bindTo(
            this,
            *kClasses
                .map { it.java }
                .toTypedArray()
        )
}
