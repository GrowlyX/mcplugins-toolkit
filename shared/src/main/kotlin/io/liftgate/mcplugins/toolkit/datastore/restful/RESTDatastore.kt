package io.liftgate.mcplugins.toolkit.datastore.restful

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
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
 * @since 6/2/2023
 */
@Export
@Service
class RESTDatastore : Eager, PostConstruct, PreDestroy, Datastore<HttpClient>
{
    @Inject
    lateinit var logger: Logger

    private var client: HttpClient? = null

    @Inject
    lateinit var configuration: Configuration<RESTConfig.Model>

    override fun postConstruct()
    {
        val restConfig = configuration.instance()
        client = HttpClient(CIO) {
            engine {
                threadsCount = restConfig.threadCount
                pipelining = restConfig.pipelining
                requestTimeout = restConfig.defaultRequestTimeout
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = restConfig.jsonPrettyPrinting
                    isLenient = true
                    ignoreUnknownKeys = true
                    serializersModule = kmongoSerializationModule
                })
            }
        }
        logger.info("Loaded a CIO http client")
    }

    override fun preDestroy()
    {
        client
            ?.close()
            ?.apply {
                logger.info("Destroyed CIO http client")
            }
    }

    override fun client() = checkNotNull(client) {
        "RESTDatastore was not initialized properly! Check startup logs for any errors."
    }
}
