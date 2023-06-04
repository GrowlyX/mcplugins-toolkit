package io.liftgate.mcplugins.toolkit

import io.liftgate.mcplugins.toolkit.contracts.Eager
import org.glassfish.hk2.api.ServiceLocator
import java.io.File
import java.util.logging.Logger

/**
 * Standard template full of information and generic lifecycle events
 * shared across almost all plugin frameworks.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
interface ToolkitPlugin
{
    fun getName(): String
    fun getLogger(): Logger

    fun getDataFolder(): File

    fun getLocator(): ServiceLocator

    /**
     * Suspending plugin enable function that is called
     * after [Eager] services are instantiated and the plugin's
     * main class is injected and post-constructed.
     */
    suspend fun enable()
    {

    }

    /**
     * Blocking plugin disable function that is called
     * after services are disposed of.
     *
     * NOTE: asyncDispatcher & minecraftDispatcher with suspending
     * main classes are disposed at this point, so please use Dispatchers.IO if
     * running asynchronous tasks on shutdown.
     */
    suspend fun disable()
    {

    }
}
