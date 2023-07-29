package io.liftgate.mcplugins.toolkit.configuration

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.YamlNamingStrategy
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.hk2.BindingBuilderUtilities
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import io.liftgate.mcplugins.toolkit.pluginBinder
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.serialization.kmongoSerializationModule

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class ConfigurationFeature : CorePluginFeature
{
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        pluginBinder(plugin) {
            bind(Yaml(
                serializersModule = kmongoSerializationModule,
                configuration = YamlConfiguration(
                    yamlNamingStrategy = YamlNamingStrategy.SnakeCase, // dash-split keys
                    strictMode = false // ignores any unknown, or non-existent keys
                )
            )).apply {
                BindingBuilderUtilities
                    .bindTo(
                        this,
                        Yaml::class.java
                    )
            }
        }

        if (!plugin.plugin.getDataFolder().exists())
        {
            plugin.plugin.getDataFolder().mkdirs()
        }

        val configurations = plugin.locator
            .getAllServices<Configuration<*>>()

        configurations.forEach { config ->
            config.load()

            pluginBinder(plugin) {
                bind(config).apply {
                    BindingBuilderUtilities
                        .bindTo(
                            this,
                            config.javaClass
                        )
                }
            }
        }
    }
}
