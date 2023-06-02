package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.jvnet.hk2.annotations.Service

/**
 * @author Elb1to
 * @since 6/1/2023
 */
@Service
class ItemStackSerializer : Serializer<ItemStack>()
{
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("org.bukkit.inventory.ItemStack") {
            element<String>("type")
            element<Int>("amount")
            element<Short>("data")
            element<String>("meta")
        }

    override fun type() = ItemStack::class

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder) = decoder
        .decodeStructure(descriptor) {
            var type = ""
            var amount = 1
            var data = 0.toShort()
            var meta = ""

            // sequential decoding protocol
            if (decodeSequentially())
            {
                type = decodeStringElement(descriptor, 0)
                amount = decodeIntElement(descriptor, 1)
                data = decodeShortElement(descriptor, 2)
                meta = decodeStringElement(descriptor, 3)
            } else while (true)
            {
                when (val index = decodeElementIndex(descriptor))
                {
                    0 -> type = decodeStringElement(descriptor, 0)
                    1 -> amount = decodeIntElement(descriptor, 1)
                    2 -> data = decodeShortElement(descriptor, 2)
                    3 -> meta = decodeStringElement(descriptor, 3)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            ItemStack(
                Material.getMaterial(type) ?: throw IllegalStateException(
                    "Material type $type does not exist"
                ),
                amount, data, meta.toByte()
            )
        }

    override fun serialize(encoder: Encoder, value: ItemStack) =
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type.name)
            encodeIntElement(descriptor, 1, value.amount)
            encodeShortElement(descriptor, 2, value.durability)
            encodeStringElement(descriptor, 3, value = value.itemMeta.toString())
            endStructure(descriptor)
        }
}