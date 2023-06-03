package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class FireworkEffectSerializer : Serializer<FireworkEffect>()
{
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("org.bukkit.FireworkEffect")

    @Serializable
    data class FireworkEffectSurrogate(
        val colors: List<@Contextual Color>,
        val fadeColors: List<@Contextual Color>,
        val type: FireworkEffect.Type,
        val trail: Boolean, val flicker: Boolean
    )

    override fun type() = FireworkEffect::class

    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            FireworkEffectSurrogate.serializer()
        )
        .let {
            FireworkEffect
                .builder()
                .withColor(it.colors)
                .withFade(it.fadeColors)
                .apply {
                    if (it.flicker)
                        withFlicker()

                    if (it.trail)
                        withTrail()
                }
                .build()
        }

    override fun serialize(encoder: Encoder, value: FireworkEffect)
    {
        encoder.encodeSerializableValue(
            FireworkEffectSurrogate.serializer(),
            FireworkEffectSurrogate(
                colors = value.colors,
                fadeColors = value.fadeColors,
                type = value.type,
                trail = value.hasTrail(),
                flicker = value.hasFlicker()
            )
        )
    }
}
