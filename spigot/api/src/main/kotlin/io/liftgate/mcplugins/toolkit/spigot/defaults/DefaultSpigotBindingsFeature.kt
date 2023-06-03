package io.liftgate.mcplugins.toolkit.spigot.defaults

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.bindTo
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.pluginBinder
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scoreboard.ScoreboardManager
import org.jvnet.hk2.annotations.Service
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class DefaultSpigotBindingsFeature : CorePluginFeature
{
    override fun rank() = 30

    @Suppress("UNCHECKED_CAST")
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        pluginBinder(plugin) {
            bind(plugin.plugin as ToolkitSpigotPlugin)
                .bindTo(
                    ToolkitPlugin::class,
                    ToolkitSpigotPlugin::class,
                    SuspendingJavaPlugin::class,
                    JavaPlugin::class,
                    Plugin::class,
                    plugin.plugin::class as KClass<in ToolkitSpigotPlugin>
                )

            bind(Bukkit.getServer()).bindTo(Server::class)
            bind(Bukkit.getPluginManager()).bindTo(PluginManager::class)
            bind(Bukkit.getScheduler()).bindTo(BukkitScheduler::class)

            // assuming a world has loaded at this point?
            Bukkit.getScoreboardManager()
                ?.apply {
                    bind(this).bindTo(ScoreboardManager::class)
                }
        }
    }
}
