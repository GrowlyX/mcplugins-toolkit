package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import org.bukkit.Bukkit
import org.bukkit.Location
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
object LocationSerializer : Serializer<Location>()
{
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Location") {
            element<Double>("x")
            element<Double>("y")
            element<Double>("z")
            element<Float>("yaw")
            element<Float>("pitch")
            element<String>("world")
        }

    override fun type() = Location::class

    override fun deserialize(decoder: Decoder) = decoder
        .decodeStructure(descriptor) {
            val world = decodeStringElement(descriptor, 0)

            Location(
                Bukkit.getWorld(world)
                    ?: throw IllegalStateException(
                        "World $world is not loaded on this server"
                    ),
                decodeDoubleElement(descriptor, 0),
                decodeDoubleElement(descriptor, 1),
                decodeDoubleElement(descriptor, 2),
                decodeFloatElement(descriptor, 0),
                decodeFloatElement(descriptor, 1)
            )
        }

    override fun serialize(encoder: Encoder, value: Location) =
        encoder.encodeStructure(descriptor) {
            encodeDoubleElement(descriptor, 0, value.x)
            encodeDoubleElement(descriptor, 1, value.y)
            encodeDoubleElement(descriptor, 2, value.z)

            encodeFloatElement(descriptor, 0, value.yaw)
            encodeFloatElement(descriptor, 1, value.pitch)

            encodeStringElement(
                descriptor, 0,
                value.world?.name
                    ?: throw IllegalStateException(
                        "World is not loaded"
                    )
            )
        }
}
