package io.liftgate.mcplugins.toolkit.spigot

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
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
        // Create plugin-specific ServiceLocator
        // prior to any enable events being called
        container
    }

    override suspend fun onEnableAsync()
    {
        // something did not go well during startup
        if (!container.onEnable())
        {
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
        }
    }

    override fun getDependencies() = description.depend.toList()
    override fun getSoftDependencies() = description.softDepend.toList()
}
