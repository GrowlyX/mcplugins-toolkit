package io.liftgate.mcplugins.toolkit.datastore.redis

import gg.scala.aware.AwareHub
import gg.scala.aware.uri.WrappedAwareUri
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import io.liftgate.mcplugins.toolkit.configuration.Configuration
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.Datastore
import io.liftgate.mcplugins.toolkit.export.Export
import jakarta.inject.Inject
import kotlinx.serialization.json.Json
import org.glassfish.hk2.api.PostConstruct
import org.glassfish.hk2.api.PreDestroy
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.serialization.kmongoSerializationModule
import java.util.logging.Logger

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Export
@Service
class RedisDatastore : PostConstruct, PreDestroy, Datastore<RedisFactory>, Eager
{
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var configuration: Configuration<RedisConfig.Model>

    @Inject
    lateinit var plugin: ToolkitPlugin

    private var factory: RedisFactory? = null

    override fun postConstruct()
    {
        val config = configuration.instance()
        if (!config.enabled)
        {
            logger.info("Skipping datastore initialization for Redis. This may cause plugin compatibility issues!")
            return
        }

        val jsonInstance = Json {
            prettyPrint = config.serialization.prettyPrinting
            isLenient = true
            ignoreUnknownKeys = true
            serializersModule = kmongoSerializationModule
        }

        AwareHub.jsonInstance = jsonInstance
        AwareHub.configure(
            WrappedAwareUri(
                address = config.address,
                port = config.port,
                password = if (config.authentication) config.password else null,
                index = config.database
            )
        )

        // initializes the RedisClient internally
        AwareHub.client()

        factory = RedisFactory(
            logger = this.logger,
            // caches a shared redis connection for grabbing/updating generic Strings
            localKVConnection = AwareHub.client().connect()
        )
        logger.info("Loaded Redis services")
    }

    override fun preDestroy()
    {
        AwareHub.close()
    }

    override fun client() = checkNotNull(factory) {
        "RedisDatastore was not initialized properly! Check startup logs for any errors."
    }
}
