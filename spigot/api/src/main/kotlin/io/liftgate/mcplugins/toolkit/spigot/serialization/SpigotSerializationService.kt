package io.liftgate.mcplugins.toolkit.spigot.serialization

import io.liftgate.mcplugins.toolkit.contracts.Eager
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.util.Vector
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.serialization.registerModule

/**
 * Binds [ConfigurationSerializable] serializers
 * to KMongo's shared SerializersModule.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class SpigotSerializationService : PostConstruct, Eager
{
    override fun postConstruct()
    {
        val serializers = SerializersModule {
            contextual(build<PotionEffect>())
            contextual(build<Location>())
            contextual(build<ItemStack>())
            contextual(build<Vector>())
            contextual(build<ItemMeta>())
            contextual(build<Color>())
            contextual(build<FireworkEffect>())
            contextual(build<OfflinePlayer>())
        }
        registerModule(serializers)
    }
}
