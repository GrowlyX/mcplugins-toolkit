package io.liftgate.mcplugins.toolkit

import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.descriptor.DescriptorProcessor
import io.liftgate.mcplugins.toolkit.export.Export
import io.liftgate.mcplugins.toolkit.export.ExportedServices
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeatures
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import io.liftgate.mcplugins.toolkit.kompat.getService
import kotlinx.coroutines.runBlocking
import org.glassfish.hk2.api.DynamicConfigurationService
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.api.ServiceLocatorFactory
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder
import org.jvnet.hk2.annotations.Contract
import java.io.IOException
import java.util.logging.Level

/**
 * Container housing services and managing lifecycle
 * events for a particular [ToolkitPlugin].
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
class ToolkitPluginContainer(
    val plugin: ToolkitPlugin
)
{
    val locator: ServiceLocator = ServiceLocatorFactory
        .getInstance()
        .create(plugin.getName())

    fun onDisable()
    {
        CorePluginFeatures
            .forEach {
                it.preDisable(this)
            }

        locator.shutdown()
        runBlocking {
            plugin.disable()
        }
    }

    fun onLoad(): Boolean
    {
        // load descriptors onLoad prior to any
        // plugin enable lifecycle events
        return loadDescriptors()
    }

    fun onEnable(): Boolean
    {
        // add local core features (if there are any)
        CorePluginFeatures += locator
            .getAllServices<CorePluginFeature>()

        CorePluginFeatures
            .forEach {
                it.preEnable(this)
            }

        // add exported services into our plugin-specific
        // ServiceLocator via dcs
        val dcs = locator
            .getService<DynamicConfigurationService>()
        dcs.createDynamicConfiguration()
            .apply {
                ExportedServices
                    .forEach { descriptor ->
                        addActiveDescriptor(descriptor)
                    }

                commit()
            }

        // add our own exported services AFTER
        // existing exports are applied.
        ExportedServices +=
            getDescriptors<Export>()

        // instantiate eager services on startup & inject the base plugin container
        locator.getAllServices(Eager::class.java)
        locator.inject(plugin)
        locator.postConstruct(plugin)

        CorePluginFeatures
            .forEach {
                it.postEnable(this)
            }

        return runBlockingUnsafe({
            plugin.getLogger().log(
                Level.SEVERE,
                "Failed to enable plugin, shutting down",
                it
            )
        }) {
            plugin.enable()
            ToolkitPluginRegistry.add(plugin)
        }
    }

    private fun loadDescriptors(): Boolean
    {
        val processors = locator.getAllServices<DescriptorProcessor>()
        val dcs = locator.getService<DynamicConfigurationService>()

        try
        {
            dcs.populator.populate(
                ClasspathDescriptorFileFinder(this::class.java.classLoader),
                *processors.toTypedArray()
            )
        } catch (exception: IOException)
        {
            plugin.getLogger().log(
                Level.SEVERE,
                "Failed to populate service with services, shutting down",
                exception
            )
            return false
        }

        return true
    }
}

