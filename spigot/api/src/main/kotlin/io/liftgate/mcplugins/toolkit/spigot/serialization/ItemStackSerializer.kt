package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.serialization.Serializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.potion.PotionEffect
import org.jvnet.hk2.annotations.Service

/**
 * @author Elb1to, GrowlyX
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
        val damage: Short,
        val itemMeta: ItemMetaSurrogate? = null
    )

    @Serializable
    data class ItemMetaSurrogate(
        val displayName: String? = null,
        val lore: List<String>? = null,
        val flags: List<ItemFlag> = listOf(),
        val enchantments: Map<String, Int> = mutableMapOf(),
        val specificItemMeta: SpecificItemMetaSurrogate? = null
    )

    @Serializable
    sealed class SpecificItemMetaSurrogate
    {
        @Serializable
        data class Leather(val color: @Contextual Color) : SpecificItemMetaSurrogate()

        @Serializable
        data class Skull(val owner: String?) : SpecificItemMetaSurrogate()

        @Serializable
        data class Potion(
            val effects: List<@Contextual PotionEffect>
        ) : SpecificItemMetaSurrogate()

        @Serializable
        data class Book(
            val title: String?,
            val author: String?,
            val pages: List<String>
        ) : SpecificItemMetaSurrogate()

        @Serializable
        data class Map(val scaling: Boolean) : SpecificItemMetaSurrogate()

        @Serializable
        data class Enchantment(
            val storedEnchants: MutableMap<String, Int>
        ) : SpecificItemMetaSurrogate()
    }

    override fun type() = ItemStack::class

    override fun deserialize(decoder: Decoder) = decoder
        .decodeSerializableValue(
            ItemStackSurrogate.serializer()
        )
        .let {
            val itemStack = ItemStack(
                Material.getMaterial(it.id)
                    ?: throw IllegalStateException(
                        "Material by ID ${it.id} does not exist"
                    ),
                it.amount
            )
            itemStack.durability = it.damage

            val itemMeta = itemStack.itemMeta
            if (itemMeta != null && it.itemMeta != null)
            {
                itemMeta.setDisplayName(it.itemMeta.displayName)
                itemMeta.lore = it.itemMeta.lore
                itemMeta.addItemFlags(
                    *it.itemMeta.flags.toTypedArray()
                )

                val specific = it.itemMeta.specificItemMeta

                if (specific != null)
                {
                    when (itemMeta)
                    {
                        is LeatherArmorMeta ->
                        {
                            val leather = specific as SpecificItemMetaSurrogate.Leather
                            itemMeta.setColor(leather.color)
                        }

                        is SkullMeta ->
                        {
                            val skull = specific as SpecificItemMetaSurrogate.Skull
                            itemMeta.setOwner(skull.owner)
                        }

                        is BookMeta ->
                        {
                            val book = specific as SpecificItemMetaSurrogate.Book
                            itemMeta.setTitle(book.title)
                            itemMeta.setPages(*book.pages.toTypedArray())
                            itemMeta.author = book.author
                        }

                        is MapMeta ->
                        {
                            val map = specific as SpecificItemMetaSurrogate.Map
                            itemMeta.isScaling = map.scaling
                        }

                        is PotionMeta ->
                        {
                            val potion = specific as SpecificItemMetaSurrogate.Potion
                            for (effect in potion.effects)
                            {
                                itemMeta.addCustomEffect(effect, true)
                            }
                        }

                        is EnchantmentStorageMeta ->
                        {
                            val enchantment = specific as SpecificItemMetaSurrogate.Enchantment
                            enchantment.storedEnchants
                                .mapKeys { entry ->
                                    Enchantment.getByName(entry.key)
                                        ?: throw IllegalStateException(
                                            "Enchantment by name ${entry.key} does not exist"
                                        )
                                }
                                .forEach { (t, u) ->
                                    itemMeta.addStoredEnchant(t, u, true)
                                }
                        }
                    }
                }

                itemStack.itemMeta = itemMeta
            }

            itemStack
        }

    override fun serialize(encoder: Encoder, value: ItemStack)
    {
        val specificMetaSurrogate =
            when (val itemMeta = value.itemMeta)
            {
                is LeatherArmorMeta -> SpecificItemMetaSurrogate.Leather(
                    color = itemMeta.color
                )

                is SkullMeta -> SpecificItemMetaSurrogate.Skull(
                    owner = itemMeta.owner
                )

                is BookMeta -> SpecificItemMetaSurrogate.Book(
                    title = itemMeta.title,
                    author = itemMeta.author,
                    pages = itemMeta.pages
                )

                is MapMeta -> SpecificItemMetaSurrogate.Map(
                    scaling = itemMeta.isScaling
                )

                is PotionMeta -> SpecificItemMetaSurrogate.Potion(
                    effects = itemMeta.customEffects
                )

                is EnchantmentStorageMeta -> SpecificItemMetaSurrogate.Enchantment(
                    storedEnchants = itemMeta.storedEnchants
                        .mapKeys { it.key.name }
                        .toMutableMap()
                )

                else -> null
            }

        encoder.encodeSerializableValue(
            ItemStackSurrogate.serializer(),
            ItemStackSurrogate(
                value.type.name,
                value.amount,
                value.durability,
                if (value.itemMeta == null)
                {
                    null
                } else
                {
                    ItemMetaSurrogate(
                        displayName = value.itemMeta!!.displayName,
                        lore = value.itemMeta!!.lore,
                        flags = value.itemMeta!!.itemFlags.toList(),
                        enchantments = value.itemMeta!!.enchants
                            .mapKeys {
                                it.key.name
                            },
                        specificItemMeta = specificMetaSurrogate
                    )
                }
            )
        )
    }
}
