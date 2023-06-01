package io.liftgate.mcplugins.toolkit.spigot.commands

import co.aikar.commands.PaperCommandManager
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.commands.ToolkitCommand
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getServiceNullable
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class SpigotCommandsFeature : CorePluginFeature
{
    override fun lazy() = true
    override fun configure(plugin: ToolkitPluginContainer)
    {
        val mcPlugin = plugin.plugin as ToolkitSpigotPlugin

        val manager = PaperCommandManager(mcPlugin)
        manager.enableUnstableAPI("help")

        bind(plugin) {
            bind(manager)
                .to(manager.javaClass)
        }

        plugin.locator
            .getAllServices(SpigotCommandManagerCustomizer::class.java)
            .forEach {
                it.customize(manager)
            }

        plugin.locator
            .getAllServices(ToolkitCommand::class.java)
            .forEach {
                manager.registerCommand(it)
            }
    }

    override fun disable(plugin: ToolkitPluginContainer)
    {
        val commandManager = plugin.locator
            .getServiceNullable<PaperCommandManager>()
            ?: return

        commandManager.unregisterCommands()
    }
}
