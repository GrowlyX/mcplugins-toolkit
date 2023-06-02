package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import org.bukkit.util.Vector
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
object VectorSerializer : Serializer<Vector>()
{
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("org.bukkit.util.Vector") {
            element<Double>("x")
            element<Double>("y")
            element<Double>("z")
        }

    override fun type() = Vector::class

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder) = decoder
        .decodeStructure(descriptor) {
            var x = 0.0
            var y = 0.0
            var z = 0.0

            if (decodeSequentially()) // sequential decoding protocol
            {
                x = decodeDoubleElement(descriptor, 0)
                y = decodeDoubleElement(descriptor, 1)
                z = decodeDoubleElement(descriptor, 2)
            } else while (true)
            {
                when (val index = decodeElementIndex(descriptor))
                {
                    0 -> x = decodeDoubleElement(descriptor, 0)
                    1 -> y = decodeDoubleElement(descriptor, 1)
                    2 -> z = decodeDoubleElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            Vector(x, y, z)
        }

    override fun serialize(encoder: Encoder, value: Vector) =
        encoder.encodeStructure(descriptor) {
            encodeDoubleElement(descriptor, 0, value.x)
            encodeDoubleElement(descriptor, 1, value.y)
            encodeDoubleElement(descriptor, 2, value.z)
        }
}
