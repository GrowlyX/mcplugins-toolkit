package io.liftgate.mcplugins.toolkit.platform.velocity.localizer

import com.velocitypowered.api.proxy.Player
import io.liftgate.localize.Localizer
import io.liftgate.localize.MappingRegistry
import io.liftgate.localize.identity.Identity
import io.liftgate.localize.identity.IdentityImpl
import io.liftgate.localize.placeholder.PlaceholderProcessor
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.platform.velocity.ToolkitVelocityPlatformPlugin
import jakarta.inject.Inject
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service
import java.util.*

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

        Localizer
            .configure(
                object : IdentityImpl
                {
                    fun Player.identity0() = object : Identity
                    {
                        override fun identifier() = uniqueId

                        override fun player() = this@identity0

                        override fun sendMessage(message: String)
                        {
                            this@identity0.sendMessage(
                                LegacyComponentSerializer
                                    .legacySection()
                                    .deserialize(message)
                            )
                        }

                        override fun username() = this@identity0.username
                    }

                    override fun identity(uniqueId: UUID) = plugin.server
                        .getPlayer(uniqueId)
                        .get().identity0()

                    override fun identity(username: String) = plugin.server
                        .getPlayer(username)
                        .get().identity0()
                },
                object : PlaceholderProcessor
                {
                    override fun transform(identity: Identity?, message: String) = message
                }
            ) {
                TODO()
            }
    }
}
