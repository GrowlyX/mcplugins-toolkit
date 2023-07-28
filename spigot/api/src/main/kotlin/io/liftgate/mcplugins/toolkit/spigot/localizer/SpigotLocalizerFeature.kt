package io.liftgate.mcplugins.toolkit.spigot.localizer

import io.liftgate.localize.Localizer
import io.liftgate.localize.MappingRegistry
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import io.liftgate.mcplugins.toolkit.localizer.LocalizationTemplate
import org.bukkit.World
import org.bukkit.entity.Player
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
@Service
class SpigotLocalizerFeature : CorePluginFeature
{
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        plugin.locator
            .getAllServices<LocalizationTemplate>()
            .forEach {
                // TODO: resource buckets are created with the toolkit's plugin files
                Localizer.of(it.javaClass.kotlin)
            }
    }
}
