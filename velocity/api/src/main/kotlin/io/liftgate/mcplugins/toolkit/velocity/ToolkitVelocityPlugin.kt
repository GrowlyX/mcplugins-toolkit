package io.liftgate.mcplugins.toolkit.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.ProxyServer
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import java.io.File
import java.nio.file.Path
import java.util.logging.Logger

/**
 * Template for any Toolkit velocity plugins.
 *
 * Two injection frameworks are used here: HK2, and Guice. The Guice injector
 * is thrown away after the initial plugin class injection, so no
 * bridging needs to be done between HK2 and Guice.
 *
 * Load/initialize and enable is treated as one on Velocity, so we use the
 * exceptions [HK2FailedToLoadDescriptorsException] and [PluginFailedToEnableException]
 * to signify what went wrong, if anything, on plugin start.
 *
 * MCCoroutine dispatchers are present in the injected [pluginContainer].
 *
 * @author GrowlyX
 * @since 6/4/2023
 */
abstract class ToolkitVelocityPlugin(
    suspendingPluginContainer: SuspendingPluginContainer,
    directory: Path
) : ToolkitPlugin
{
    @Inject lateinit var pluginContainer: PluginContainer
    @Inject lateinit var _logger: Logger
    @Inject lateinit var server: ProxyServer

    private val toolkitContainer by lazy {
        ToolkitPluginContainer(this)
    }

    private val _dataFolder = directory.toFile()
    private var pluginEnabled = false

    init
    {
        suspendingPluginContainer
            .initialize(this)
    }

    @Subscribe
    fun ProxyInitializeEvent.on()
    {
        // Create plugin-specific ServiceLocator
        // prior to any enable events being called
        if (!toolkitContainer.onLoad())
            throw HK2FailedToLoadDescriptorsException(this@ToolkitVelocityPlugin)

        // something did not go well during startup
        if (!toolkitContainer.onEnable())
            throw PluginFailedToEnableException(this@ToolkitVelocityPlugin)

        pluginEnabled = true
    }

    @Subscribe
    fun ProxyShutdownEvent.on()
    {
        if (pluginEnabled)
        {
            toolkitContainer.onDisable()
        }
    }

    override fun getName() = pluginContainer.description.name.get()
    override fun getLogger() = _logger
    override fun getDataFolder(): File = _dataFolder

    override fun getLocator() = toolkitContainer.locator
}
