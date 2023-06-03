package io.liftgate.mcplugins.toolkit.descriptor

import io.liftgate.mcplugins.toolkit.platform
import io.liftgate.mcplugins.toolkit.softdepend.SoftDepend
import org.glassfish.hk2.utilities.DescriptorImpl

/**
 * Filters out services whose dependant plugin
 * is not enabled on the server.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
class SoftDependQualifierProcessor : QualifierProcessor(SoftDepend::class)
{
    override fun process(
        descriptor: DescriptorImpl
    ): DescriptorImpl?
    {
        val metadata = descriptor
            .metadata["SoftDepend"]?.first()
            ?: return null

        return if (platform.hasPlugin(metadata))
            descriptor else null
    }
}
