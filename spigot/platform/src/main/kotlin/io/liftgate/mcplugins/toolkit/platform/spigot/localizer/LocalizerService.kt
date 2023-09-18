package io.liftgate.mcplugins.toolkit.platform.spigot.localizer

import io.liftgate.localize.Localizer
import io.liftgate.localize.MappingRegistry
import io.liftgate.localize.placeholder.PlaceholderProcessor
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.platform.spigot.ToolkitSpigotPlatformPlugin
import jakarta.inject.Inject
import org.bukkit.World
import org.bukkit.entity.Player
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
@Service
class LocalizerService : PostConstruct, Eager
{
    @Inject
    lateinit var plugin: ToolkitSpigotPlatformPlugin

    override fun postConstruct()
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

        Localizer.configure { TODO() }
    }
}
