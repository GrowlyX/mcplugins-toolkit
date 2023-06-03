package io.liftgate.mcplugins.toolkit.configuration

import kotlin.reflect.KClass

/**
 * Kotlin doesn't seem to recognize that KClass<A> (implementing B)
 * is the same as KClass<in B>. This cast fixes it.
 *
 * @author GrowlyX
 * @since 6/3/2023
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<T>.castToAny(): KClass<in Any>
{
    return this as KClass<in Any>
}
