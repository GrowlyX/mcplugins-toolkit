package io.liftgate.mcplugins.toolkit.spigot.defaults

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.hk2.BindingBuilderUtilities
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class DefaultSpigotBindingsFeature : CorePluginFeature
{
    override fun rank() = 30
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        bind(plugin) {
            bind(plugin.plugin as ToolkitSpigotPlugin)
                .apply {
                    BindingBuilderUtilities
                        .bindTo(
                            this,
                            ToolkitPlugin::class.java,
                            ToolkitSpigotPlugin::class.java,
                            SuspendingJavaPlugin::class.java,
                            JavaPlugin::class.java,
                            Plugin::class.java,
                        )
                }

            bind(Bukkit.getServer())
                .apply {
                    BindingBuilderUtilities
                        .bindTo(
                            this,
                            Server::class.java
                        )
                }

            bind(Bukkit.getPluginManager())
                .apply {
                    BindingBuilderUtilities
                        .bindTo(
                            this,
                            PluginManager::class.java
                        )
                }
        }
    }
}
