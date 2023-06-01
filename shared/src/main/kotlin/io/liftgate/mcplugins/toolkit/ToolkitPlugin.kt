package io.liftgate.mcplugins.toolkit

import io.liftgate.mcplugins.toolkit.contracts.Eager
import org.glassfish.hk2.api.ServiceLocator
import java.util.logging.Logger

/**
 * Standard template full of information shared
 * across almost all plugin frameworks.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
interface ToolkitPlugin
{
    fun getName(): String
    fun getLogger(): Logger

    fun getDependencies(): List<String>
    fun getSoftDependencies(): List<String>

    fun getLocator(): ServiceLocator

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
}
