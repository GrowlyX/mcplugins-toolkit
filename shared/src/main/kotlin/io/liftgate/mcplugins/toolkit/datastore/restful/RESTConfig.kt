package io.liftgate.mcplugins.toolkit.datastore.restful

import io.liftgate.mcplugins.toolkit.configuration.Configuration
import kotlinx.serialization.Serializable
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class RESTConfig : Configuration<RESTConfig.Model>()
{
    @Serializable
    data class Model(
        val threadCount: Int = 2,
        val defaultRequestTimeout: Long = 10_000L,
        val pipelining: Boolean = false,
        val jsonPrettyPrinting: Boolean = false
    )

    override fun fileName() = "httpclient.yml"
    override fun type() = Model::class
}
