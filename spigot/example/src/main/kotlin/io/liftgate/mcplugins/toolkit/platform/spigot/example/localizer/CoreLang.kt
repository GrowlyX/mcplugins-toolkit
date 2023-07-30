package io.liftgate.mcplugins.toolkit.platform.spigot.example.localizer

import io.liftgate.localize.annotate.*
import io.liftgate.localize.identity.Identity
import io.liftgate.mcplugins.toolkit.localizer.LocalizationTemplate
import org.bukkit.entity.Player
import org.jvnet.hk2.annotations.Service
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/28/2023
 */
interface CoreLang
{
    @Colored
    @Id("player_login")
    @Describe("Broadcasts this message to the server when a player logs in!")
    @DefaultsTo("&a%player% joined the game!")
    fun playerJoins(
        @Self
        @Component("username")
        player: Identity
    ): List<String>

    @Colored
    @Describe("Broadcasts this message to the server when a player logs out!")
    @DefaultsTo("&e%player% left the game for %reason%!")
    fun playerLeaves(
        @Self
        player: Identity,
        reason: String
    ): List<String>

    @Colored
    @DefaultsTo("&b[Staff] &b%username% &3left the server.")
    fun staffLogin(username: String): List<String>
}

@Service
class CoreLangService : LocalizationTemplate
{
    override val langClass: KClass<*> = CoreLang::class
}
