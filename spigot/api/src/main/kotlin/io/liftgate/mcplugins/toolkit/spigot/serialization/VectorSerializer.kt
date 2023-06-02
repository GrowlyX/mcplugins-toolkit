package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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

    @Serializable
    data class VectorSurrogate(
        val x: Double, val y: Double, val z: Double
    )

    override fun type() = Vector::class

    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            VectorSurrogate.serializer()
        )
        .let {
            Vector(it.x, it.y, it.z)
        }

    override fun serialize(encoder: Encoder, value: Vector)
    {
        encoder.encodeSerializableValue(
            VectorSurrogate.serializer(),
            VectorSurrogate(value.x, value.y, value.z)
        )
    }
}
