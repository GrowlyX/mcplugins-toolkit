package io.liftgate.mcplugins.toolkit.serialization

import kotlinx.serialization.KSerializer
import org.jvnet.hk2.annotations.Contract
import kotlin.reflect.KClass

/**
 * Contract marking any custom KSerializers needing to be
 * registered into KMongo's serializer mappings as a SerializerModule.
 *
 * @author GrowlyX
 * @since 6/1/2023
 */
@Contract
abstract class Serializer<T : Any> : KSerializer<T>
{
    abstract fun type(): KClass<T>
}
