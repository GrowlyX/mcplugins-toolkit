package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.World
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
object WorldSerializer : Serializer<World>()
{
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("org.bukkit.World", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) =
        decoder.decodeString()
            .let {
                Bukkit.getWorld(it)
                    ?: throw IllegalStateException(
                        "World by name $it is not loaded"
                    )
            }

    override fun serialize(encoder: Encoder, value: World) =
        encoder.encodeString(value.name)

    override fun type() = World::class
}
