package io.liftgate.mcplugins.toolkit.serialization

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import kotlinx.serialization.modules.SerializersModule
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.serialization.registerModule

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class SerializationFeature : CorePluginFeature
{
    override fun rank() = 100
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        val serializers = plugin.locator
            .getAllServices<Serializer<*>>()

        val customSerializerModule = SerializersModule {
            serializers.forEach { serializer ->
                contextual(serializer.type()) { serializer }
            }
        }

        registerModule(customSerializerModule)
    }
}
