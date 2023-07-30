package io.liftgate.mcplugins.toolkit.datastore.redis

import io.liftgate.mcplugins.toolkit.configuration.Configuration
import kotlinx.serialization.Serializable
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class RedisConfig : Configuration<RedisConfig.Model>()
{
    @Serializable
    data class Model(
        val enabled: Boolean = false,
        val address: String = "127.0.0.1",
        val port: Int = 6379,
        val authentication: Boolean = false,
        val password: String? = null,
        val database: Int = 0,
        val serialization: Serialization = Serialization()
    )
    {
        @Serializable
        data class Serialization(val prettyPrinting: Boolean = true)
    }

    override fun fileName() = "redis.yml"
    override fun type() = Model::class
}
