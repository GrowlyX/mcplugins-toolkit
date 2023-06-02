package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
        buildClassSerialDescriptor("org.bukkit.inventory.ItemStack")

    @Serializable
    data class ItemStackSurrogate(
        val id: String,
        val amount: Int,
        val durability: Short
    )

    override fun type() = ItemStack::class

    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            ItemStackSurrogate.serializer()
        )
        .let {
            ItemStack(
                Material.getMaterial(it.id) ?: throw IllegalStateException(
                    "Material by ID ${it.id} does not exist"
                ),

                it.amount, it.durability
            )
        }

    override fun serialize(encoder: Encoder, value: ItemStack)
    {
        encoder.encodeSerializableValue(
            ItemStackSurrogate.serializer(),
            ItemStackSurrogate(
                value.type.name,
                value.amount,
                value.durability
            )
        )
    }
}