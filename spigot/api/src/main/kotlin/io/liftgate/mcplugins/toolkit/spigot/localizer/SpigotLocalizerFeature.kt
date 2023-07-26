package io.liftgate.mcplugins.toolkit.spigot.localizer

import io.liftgate.localize.Localizer
import io.liftgate.localize.MappingRegistry
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import io.liftgate.mcplugins.toolkit.localizer.LocalizationTemplate
import org.bukkit.World
import org.bukkit.entity.Player

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
class SpigotLocalizerFeature : CorePluginFeature
{
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        MappingRegistry.apply {
            registerDefaultComponent<Player>(Player::getName)
            registerDefaultComponent<World>(World::getName)

            registerComponent<Player>("name", Player::getName)
            registerComponent<Player>("health") { it.health.toString() }
            registerComponent<Player>("uuid") { it.uniqueId.toString() }
            registerComponent<Player>("displayName") { it.displayName }
            registerComponent<Player>("ping") { it.ping.toString() }
        }

        plugin.locator
            .getAllServices<LocalizationTemplate>()
            .forEach {
                Localizer.of(it.javaClass.kotlin)
            }
    }
}
