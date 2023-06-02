package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class PotionEffectSerializer : Serializer<PotionEffect>()
{
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("org.bukkit.potion.PotionEffect") {
            element<Int>("id")
            element<Int>("duration")
            element<Int>("amplifier")
            element<Boolean>("ambient")
        }

    override fun type() = PotionEffect::class

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder) = decoder
        .decodeStructure(descriptor) {
            var type = -1
            var duration = -1
            var amplifier = -1
            var ambient = false

            if (decodeSequentially()) // sequential decoding protocol
            {
                type = decodeIntElement(descriptor, 0)
                duration = decodeIntElement(descriptor, 1)
                amplifier = decodeIntElement(descriptor, 2)
                ambient = decodeBooleanElement(descriptor, 3)
            } else while (true)
            {
                when (val index = decodeElementIndex(descriptor))
                {
                    0 -> type = decodeIntElement(descriptor, 0)
                    1 -> duration = decodeIntElement(descriptor, 1)
                    2 -> amplifier = decodeIntElement(descriptor, 2)
                    3 -> ambient = decodeBooleanElement(descriptor, 3)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            PotionEffect(
                PotionEffectType.getById(type)
                    ?: throw IllegalStateException(
                        "Potion effect type $type does not exist"
                    ),
                duration, amplifier, ambient
            )
        }

    override fun serialize(encoder: Encoder, value: PotionEffect) =
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.type.id)
            encodeIntElement(descriptor, 1, value.duration)
            encodeIntElement(descriptor, 2, value.amplifier)
            encodeBooleanElement(descriptor, 3, value.isAmbient)
            endStructure(descriptor)
        }
}
