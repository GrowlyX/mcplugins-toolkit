package io.liftgate.mcplugins.toolkit.platform.velocity.localizer

import com.velocitypowered.api.proxy.Player
import io.liftgate.localize.Localizer
import io.liftgate.localize.MappingRegistry
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.platform.velocity.ToolkitVelocityPlatformPlugin
import jakarta.inject.Inject
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
    lateinit var plugin: ToolkitVelocityPlatformPlugin

    override fun postConstruct()
    {
        MappingRegistry.apply {
            registerDefaultComponent<Player>(Player::getUsername)

            registerComponent<Player>("name", Player::getUsername)
            registerComponent<Player>("uuid") { it.uniqueId.toString() }
            registerComponent<Player>("ping") { it.ping.toString() }
        }

        Localizer.configure { TODO() }
    }
}
