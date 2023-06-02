package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
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
        buildClassSerialDescriptor("org.bukkit.Location") {
            element<Double>("x")
            element<Double>("y")
            element<Double>("z")
            element<Float>("yaw")
            element<Float>("pitch")
            element<String>("world")
        }

    override fun type() = Location::class

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder) = decoder
        .decodeStructure(descriptor) {
            var world: String? = null
            var x = 0.0
            var y = 0.0
            var z = 0.0
            var yaw = 0.0f
            var pitch = 0.0f

            if (decodeSequentially()) // sequential decoding protocol
            {
                x = decodeDoubleElement(descriptor, 0)
                y = decodeDoubleElement(descriptor, 1)
                z = decodeDoubleElement(descriptor, 2)
                yaw = decodeFloatElement(descriptor, 3)
                pitch = decodeFloatElement(descriptor, 4)
                world = decodeStringElement(descriptor, 5)
            } else while (true)
            {
                when (val index = decodeElementIndex(descriptor))
                {
                    0 -> x = decodeDoubleElement(descriptor, 0)
                    1 -> y = decodeDoubleElement(descriptor, 1)
                    2 -> z = decodeDoubleElement(descriptor, 2)
                    3 -> yaw = decodeFloatElement(descriptor, 3)
                    4 -> pitch = decodeFloatElement(descriptor, 4)
                    5 -> world = decodeStringElement(descriptor, 5)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            Location(
                Bukkit
                    .getWorld(
                        world
                            ?: throw IllegalStateException(
                                "World was not decoded properly"
                            )
                    )
                    ?: throw IllegalStateException(
                        "World $world is not loaded on this server"
                    ),
                x, y, z, yaw, pitch
            )
        }

    override fun serialize(encoder: Encoder, value: Location) =
        encoder.encodeStructure(descriptor) {
            encodeDoubleElement(descriptor, 0, value.x)
            encodeDoubleElement(descriptor, 1, value.y)
            encodeDoubleElement(descriptor, 2, value.z)

            encodeFloatElement(descriptor, 3, value.yaw)
            encodeFloatElement(descriptor, 4, value.pitch)

            encodeStringElement(
                descriptor, 5,
                value.world?.name
                    ?: throw IllegalStateException(
                        "World is not loaded"
                    )
            )
            endStructure(descriptor)
        }
}
