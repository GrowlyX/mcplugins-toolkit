package io.liftgate.mcplugins.toolkit.spigot

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import io.liftgate.mcplugins.toolkit.contracts.Eager
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.utilities.ServiceLocatorUtilities
import org.glassfish.hk2.utilities.binding.AbstractBinder
import java.util.logging.Logger

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
abstract class ToolkitPlugin : SuspendingJavaPlugin()
{
    private lateinit var locator: ServiceLocator

    /**
     * Blocking plugin enable function that is called
     * after [Eager] services are instantiated.
     */
    open suspend fun enable()
    {

    }

    /**
     * Blocking plugin disable function that is called
     * after [Eager] services are instantiated.
     *
     * asyncDispatcher & minecraftDispatcher are disposed
     * at this point, so use Dispatchers.IO.
     */
    open suspend fun disable()
    {

    }

    override fun onEnable()
    {
        locator = ServiceLocatorUtilities
            .createAndPopulateServiceLocator(
                description.name
            )

        ServiceLocatorUtilities.bind(
            locator,
            object : AbstractBinder()
            {
                override fun configure()
                {
                    bind(logger)
                        .to(Logger::class.java)

                    bind(this@ToolkitPlugin)
                        .to(JavaPlugin::class.java)
                        .to(SuspendingJavaPlugin::class.java)
                        .to(Plugin::class.java)
                        .to(this@ToolkitPlugin.javaClass)
                }
            }
        )

        // instantiate eager services on startup
        locator.getAllServices(Eager::class.java)

        runBlocking {
            enable()
        }
    }

    override fun onDisable()
    {
        locator.shutdown()
        runBlocking {
            disable()
        }
    }
}
