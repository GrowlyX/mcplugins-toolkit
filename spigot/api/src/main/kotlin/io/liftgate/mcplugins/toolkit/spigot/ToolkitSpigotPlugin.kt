package io.liftgate.mcplugins.toolkit.spigot

import com.github.shynixn.mccoroutine.bukkit.ShutdownStrategy
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.mcCoroutineConfiguration
import io.liftgate.mcplugins.toolkit.*
import io.liftgate.mcplugins.toolkit.descriptor.DescriptorProcessor
import io.liftgate.mcplugins.toolkit.spigot.version.ServerVersionQualifierProcessor
import org.bukkit.Bukkit

/**
 * An implementation of [ToolkitPlugin] which implements
 * [SuspendingJavaPlugin], allowing for suspending Spigot services.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
abstract class ToolkitSpigotPlugin : SuspendingJavaPlugin(), ToolkitPlugin
{
    private var pluginEnabled = false
    private val container by lazy {
        ToolkitPluginContainer(this)
    }

    override fun getLocator() = container.locator

    override fun onLoad()
    {
        pluginBinder(container) {
            bind(ServerVersionQualifierProcessor::class.java)
                .bindToSuper(
                    DescriptorProcessor::class
                )
        }

        // Create plugin-specific ServiceLocator
        // prior to any enable events being called
        if (!container.onLoad())
        {
            logger.info("Failed to load descriptors, disabling")
            Bukkit.getPluginManager()
                .disablePlugin(this)
        }
    }

    override suspend fun onEnableAsync()
    {
        mcCoroutineConfiguration.shutdownStrategy = ShutdownStrategy.MANUAL

        // something did not go well during startup
        if (!container.onEnable())
        {
            logger.info("Failed to load plugin, disabling")
            Bukkit.getPluginManager()
                .disablePlugin(this)
            return
        }

        // TODO: better lifecycle management
        pluginEnabled = true
    }

    override suspend fun onDisableAsync()
    {
        // We cannot guarantee services
        // have been initialized properly, so we're not
        // going to try to shut them down either.
        if (pluginEnabled)
        {
            container.onDisable()
            mcCoroutineConfiguration.disposePluginSession()
        }
    }
}
