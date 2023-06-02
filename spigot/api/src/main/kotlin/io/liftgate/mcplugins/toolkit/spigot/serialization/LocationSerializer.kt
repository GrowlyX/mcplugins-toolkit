package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class LocationSerializer : Serializer<Location>()
{
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("org.bukkit.Location")

    @Serializable
    data class LocationSurrogate(
        val x: Double, val y: Double, val z: Double,
        val yaw: Float, val pitch: Float,
        val world: String
    )

    override fun type() = Location::class

    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            LocationSurrogate.serializer()
        )
        .let {
            Location(
                Bukkit.getWorld(it.world)
                    ?: throw IllegalStateException(
                        "World was not decoded properly"
                    ),
                it.x, it.y, it.z,
                it.yaw, it.pitch
            )
        }

    override fun serialize(encoder: Encoder, value: Location)
    {
        encoder.encodeSerializableValue(
            LocationSurrogate.serializer(),
            LocationSurrogate(
                value.x, value.y, value.z,
                value.yaw, value.pitch,
                value.world?.name
                    ?: throw IllegalStateException(
                        "World is not loaded, cannot serialize world name from WorldInfo"
                    )
            )
        )
    }
}
