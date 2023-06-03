package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.util.BlockVector
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class BlockVectorSerializer : Serializer<BlockVector>()
{
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("org.bukkit.util.BlockVector")

    @Serializable
    data class BlockVectorSurrogate(
        val x: Double, val y: Double, val z: Double
    )

    override fun type() = BlockVector::class

    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            BlockVectorSurrogate.serializer()
        )
        .let {
            BlockVector(it.x, it.y, it.z)
        }

    override fun serialize(encoder: Encoder, value: BlockVector)
    {
        encoder.encodeSerializableValue(
            BlockVectorSurrogate.serializer(),
            BlockVectorSurrogate(value.x, value.y, value.z)
        )
    }
}
