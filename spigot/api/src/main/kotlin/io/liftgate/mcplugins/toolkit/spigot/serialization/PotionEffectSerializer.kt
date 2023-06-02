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
        buildClassSerialDescriptor("org.bukkit.potion.PotionEffect")

    data class PotionEffectSurrogate(
        val id: Int,
        val duration: Int,
        val amplifier: Int,
        val ambient: Boolean
    )

    override fun type() = Vector::class

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            PotionEffectSurrogate.serializer()
        )
        .apply {
            PotionEffect(id, duration, amplifier, ambient)
        }

    override fun serialize(encoder: Encoder, value: Vector)
    {
        encoder.encodeSerializableValue(
            PotionEffectSurrogate.serializer(), 
            PotionEffectSurrogate(
                value.type.id, 
                value.duration,
                value.amplifier,
                value.ambient
            )
        )
    }
}
