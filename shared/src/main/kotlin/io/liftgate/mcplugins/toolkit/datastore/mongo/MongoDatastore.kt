package io.liftgate.mcplugins.toolkit.datastore.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import io.liftgate.mcplugins.toolkit.configuration.Configuration
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.Datastore
import io.liftgate.mcplugins.toolkit.export.Export
import jakarta.inject.Inject
import org.bson.UuidRepresentation
import org.glassfish.hk2.api.PostConstruct
import org.glassfish.hk2.api.PreDestroy
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.KMongo
import java.util.logging.Logger

/**
 * Configures and exports a [CoroutineDatabase] which can be used
 * to access respective collections for the respective [Serializable] model.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Export
@Service
class MongoDatastore : PostConstruct, PreDestroy, Datastore<MongoDatabase>, Eager
{
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var configuration: Configuration<MongoConfig.Model>

    @Inject
    lateinit var plugin: ToolkitPlugin

    private var client: MongoClient? = null
    private var database: MongoDatabase? = null

    override fun postConstruct()
    {
        val config = configuration.instance()
        val clientSettings = MongoClientSettings
            .builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyConnectionString(
                ConnectionString(config.uri)
            )

        if (config.credentials.enabled)
        {
            clientSettings
                .credential(
                    MongoCredential.createCredential(
                        config.credentials.username,
                        config.credentials.database,
                        config.credentials.password.toCharArray()
                    )
                )
        }

        if (!config.enabled)
        {
            logger.info("Skipping datastore initialization for MongoDB. This may cause plugin compatibility issues!")
            return
        }

        client = KMongo
            .createClient(
                clientSettings.build()
            )

        database = client
            ?.getDatabase(config.database)
            ?.apply {
                logger.info("Loaded mongo services with database ${config.database}")
            }
            ?: run {
                logger.warning("MongoDatastore was not initialized properly! This may causes below!")
                null
            }
    }

    override fun preDestroy()
    {
        client
            ?.close()
            ?.apply {
                logger.info("Disposed of mongo services")
            }
    }

    override fun client() = checkNotNull(database) {
        "MongoDatastore was not initialized properly! Check startup logs for any errors."
    }
}
