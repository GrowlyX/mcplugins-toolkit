package io.liftgate.mcplugins.toolkit.datastore.mongo

import io.liftgate.mcplugins.toolkit.configuration.Configuration
import kotlinx.serialization.Serializable
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class MongoConfig : Configuration<MongoConfig.Model>()
{
    @Serializable
    data class Model(
        val enabled: Boolean = true,
        val uri: String = "mongodb://127.0.0.1:27017",
        val database: String = "experimental",
        val credentials: Credentials = Credentials()
    )
    {
        @Serializable
        data class Credentials(
            val enabled: Boolean = false,
            val username: String = "toolkit",
            val database: String = "admin",
            val password: String = ""
        )
    }

    override fun fileName() = "mongo.yml"
    override fun type() = Model::class
}
