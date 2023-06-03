package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Color
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class ColorSerializer : Serializer<Color>()
{
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("org.bukkit.Color", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder) =
        decoder.decodeString()
            .let {
                Color.fromRGB(
                    decoder.decodeInt()
                )
            }

    override fun serialize(encoder: Encoder, value: Color) =
        encoder.encodeInt(value.asRGB())

    override fun type() = Color::class
}
