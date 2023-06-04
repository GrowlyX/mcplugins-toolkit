package io.liftgate.mcplugins.toolkit.platform.spigot.example.commands

import io.liftgate.mcplugins.toolkit.configuration.Configuration
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class ExampleConfig : Configuration<ExampleConfig.Model>()
{
    @Serializable
    data class Model(
        val uri: @Contextual ItemStack = ItemStack(Material.BOOK)
    )

    override fun fileName() = "example.yml"
    override fun type() = Model::class
}
