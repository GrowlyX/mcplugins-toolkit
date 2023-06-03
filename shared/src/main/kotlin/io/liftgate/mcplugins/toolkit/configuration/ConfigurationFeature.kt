package io.liftgate.mcplugins.toolkit.configuration

import com.charleskorn.kaml.Yaml
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.serialization.kmongoSerializationModule
import java.io.File

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class ConfigurationFeature : CorePluginFeature
{
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        val configurations = plugin.locator
            .getAllServices<Configuration>()

        bind(plugin) {
            bind(Yaml(
                serializersModule = kmongoSerializationModule
            )).to(
                Yaml::class.java
            )
        }

        configurations.forEach { config ->
            val configFile = File(
                plugin.plugin.getDataFolder(),
                config.getFileName()
            )

            // TODO: fix inputFromStream reified type
            /**
             * plugin.locator
             *                     .getService<Yaml>()
             *                     .decodeFromStream(
             *                         configFile.inputStream()
             *                     )
             */
        }
    }
}
