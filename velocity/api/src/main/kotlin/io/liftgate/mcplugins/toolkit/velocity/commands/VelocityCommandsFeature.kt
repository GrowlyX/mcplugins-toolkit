package io.liftgate.mcplugins.toolkit.velocity.commands

import co.aikar.commands.VelocityCommandManager
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.commands.ToolkitCommand
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getServiceNullable
import io.liftgate.mcplugins.toolkit.pluginBinder
import io.liftgate.mcplugins.toolkit.velocity.commands.manager.ToolkitCommandManager
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 6/4/2023
 */
@Service
class VelocityCommandsFeature : CorePluginFeature
{
    override fun postEnable(plugin: ToolkitPluginContainer)
    {
        val mcPlugin = plugin.plugin as ToolkitVelocityPlugin

        val manager = ToolkitCommandManager(mcPlugin)
        manager.registerDefaults()

        pluginBinder(plugin) {
            bind(manager)
                .to(manager.javaClass)
        }

        plugin.locator
            .getAllServices(VelocityCommandManagerCustomizer::class.java)
            .forEach {
                it.customize(manager)
            }

        plugin.locator
            .getAllServices(ToolkitCommand::class.java)
            .forEach {
                manager.registerCommand(it)
            }
    }

    override fun preDisable(plugin: ToolkitPluginContainer)
    {
        val commandManager = plugin.locator
            .getServiceNullable<VelocityCommandManager>()
            ?: return

        commandManager.unregisterCommands()
    }
}
