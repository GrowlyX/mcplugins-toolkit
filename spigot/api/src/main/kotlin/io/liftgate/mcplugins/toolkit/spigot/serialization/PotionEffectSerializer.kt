package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
        buildClassSerialDescriptor("org.bukkit.potion.PotionEffect")

    @Serializable
    data class PotionEffectSurrogate(
        val id: Int,
        val duration: Int,
        val amplifier: Int,
        val ambient: Boolean
    )

    override fun type() = PotionEffect::class

    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            PotionEffectSurrogate.serializer()
        )
        .let {
            PotionEffect(
                PotionEffectType.getById(it.id)
                    ?: throw IllegalStateException(
                        "PotionEffectType by ID ${it.id} does not exist"
                    ),
                it.duration, it.amplifier, it.ambient
            )
        }

    override fun serialize(encoder: Encoder, value: PotionEffect)
    {
        encoder.encodeSerializableValue(
            PotionEffectSurrogate.serializer(),
            PotionEffectSurrogate(
                value.type.id,
                value.duration,
                value.amplifier,
                value.isAmbient
            )
        )
    }
}
