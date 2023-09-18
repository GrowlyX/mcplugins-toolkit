package io.liftgate.mcplugins.toolkit.platform.velocity.example

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin
import java.nio.file.Path

/**
 * @author GrowlyX
 * @since 6/4/2023
 */
@Plugin(
    id = "toolkit-example",
    name = "Toolkit-Example",
    version = "1.0.0",
    dependencies = [
        Dependency(id = "toolkit")
    ]
)
class ExampleVelocityPlugin @Inject constructor(
    @DataDirectory
    directory: Path
) : ToolkitVelocityPlugin(directory)
