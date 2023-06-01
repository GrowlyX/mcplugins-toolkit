package io.liftgate.mcplugins.toolkit.descriptor

import io.liftgate.mcplugins.toolkit.ToolkitDescriptorMetadata
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.export.Export
import jakarta.inject.Inject
import org.glassfish.hk2.utilities.DescriptorImpl
import org.jvnet.hk2.annotations.Service

/**
 * Adds in plugin metadata to an exported service.
 * TODO: add in softdepend functionality
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class ExportedServiceQualifierProcessor : QualifierProcessor(Export::class)
{
    @Inject
    lateinit var container: ToolkitPluginContainer

    override fun process(
        descriptor: DescriptorImpl
    ): DescriptorImpl
    {
        descriptor.addMetadata(
            ToolkitDescriptorMetadata.PLUGIN,
            container.plugin.getName()
        )
        return descriptor
    }
}
