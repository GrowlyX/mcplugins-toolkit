package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import org.bukkit.util.Vector
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class VectorSerializer : Serializer<Vector>()
{
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("org.bukkit.util.Vector")

    data class VectorSurrogate(
        val x: Double, val y: Double, val z: Double
    )

    override fun type() = Vector::class

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            VectorSurrogate.serializer()
        )
        .apply {
            Vector(x, y, z)
        }

    override fun serialize(encoder: Encoder, value: Vector)
    {
        encoder.encodeSerializableValue(
            VectorSurrogate.serializer(), 
            VectorSurrogate(value.x, value.y, value.z)
        )
    }
}
