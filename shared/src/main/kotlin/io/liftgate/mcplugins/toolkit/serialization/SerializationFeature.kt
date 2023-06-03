package io.liftgate.mcplugins.toolkit.serialization

import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.hk2.BindingBuilderUtilities
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.serialization.SerializationClassMappingTypeService
import org.litote.kmongo.serialization.kmongoSerializationModule
import org.litote.kmongo.serialization.registerModule

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Service
class SerializationFeature : CorePluginFeature
{
    override fun rank() = 10

    @OptIn(ExperimentalSerializationApi::class)
    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        // set mapping service to serialization before using KMongo's serialization module
        System.setProperty(
            "org.litote.mongo.mapping.service",
            SerializationClassMappingTypeService::class.qualifiedName!!
        )

        val serializers = plugin.locator
            .getAllServices<Serializer<*>>()

        val customSerializerModule = SerializersModule {
            serializers.forEach { serializer ->
                contextual(serializer.type()) { serializer }
            }
        }

        registerModule(customSerializerModule)

        bind(plugin) {
            val json = Json {
                explicitNulls = true
                encodeDefaults = true
                ignoreUnknownKeys = true
                serializersModule = kmongoSerializationModule
            }

            bind(json)
                .apply {
                    BindingBuilderUtilities
                        .bindTo(
                            this,
                            Json::class.java
                        )
                }
        }
    }
}
