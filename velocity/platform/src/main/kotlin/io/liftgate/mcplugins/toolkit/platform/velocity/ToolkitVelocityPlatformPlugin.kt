package io.liftgate.mcplugins.toolkit.platform.velocity

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin
import java.nio.file.Path

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Plugin(
    id = "toolkit",
    name = "Toolkit",
    version = "1.0.0"
)
class ToolkitVelocityPlatformPlugin @Inject constructor(
    @DataDirectory
    directory: Path
) : ToolkitVelocityPlugin(directory)
{
    companion object
    {
        @JvmStatic
        lateinit var plugin: ToolkitVelocityPlatformPlugin
    }

    override suspend fun enable()
    {
        plugin = this
    }
}
