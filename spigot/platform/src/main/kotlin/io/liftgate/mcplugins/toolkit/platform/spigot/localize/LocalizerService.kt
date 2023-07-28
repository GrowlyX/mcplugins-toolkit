package io.liftgate.mcplugins.toolkit.platform.spigot.localize

import io.liftgate.localize.Localizer
import io.liftgate.localize.MappingRegistry
import io.liftgate.localize.buckets.YamlResourceBucket
import io.liftgate.localize.identity.Identity
import io.liftgate.localize.identity.IdentityImpl
import io.liftgate.localize.placeholder.PlaceholderProcessor
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.platform.spigot.ToolkitSpigotPlatformPlugin
import jakarta.inject.Inject
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service
import java.io.File
import java.util.*

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

        Localizer
            .configure(
                object : IdentityImpl
                {
                    fun Player.identity() = object : Identity
                    {
                        override fun identifier() = uniqueId

                        override fun player() = this@identity

                        override fun sendMessage(message: String)
                        {
                            this@identity.sendMessage(message)
                        }

                        override fun username() = this@identity.name
                    }

                    override fun identity(uniqueId: UUID) = Bukkit
                        .getPlayer(uniqueId)!!
                        .identity()

                    override fun identity(username: String) = Bukkit
                        .getPlayer(username)!!
                        .identity()
                },
                object : PlaceholderProcessor
                {
                    override fun transform(identity: Identity?, message: String) = message
                }
            ) {
                YamlResourceBucket(it, File(
                    plugin.dataFolder,
                    it.java.name
                        .removeSuffix("Lang")
                        .lowercase()
                ))
            }
    }
}
