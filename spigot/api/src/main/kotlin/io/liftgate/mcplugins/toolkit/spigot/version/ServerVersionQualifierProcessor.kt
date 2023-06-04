package io.liftgate.mcplugins.toolkit.spigot.version

import io.liftgate.mcplugins.toolkit.descriptor.QualifierProcessor
import org.glassfish.hk2.utilities.DescriptorImpl

/**
 * Ensures services that depend on a specific Minecraft
 * version match the current server version.
 *
 * @author GrowlyX
 * @since 6/3/2023
 */
class ServerVersionQualifierProcessor : QualifierProcessor(RequiresVersion::class)
{
    override fun process(
        descriptor: DescriptorImpl
    ): DescriptorImpl?
    {
        val metadata = descriptor
            .metadata["ServerVersion"]?.first()
            ?: return null

        val serverVersion = ServerVersion.valueOf(metadata)

        return if (serverVersion == ServerVersion.getVersion())
            descriptor else null
    }
}
