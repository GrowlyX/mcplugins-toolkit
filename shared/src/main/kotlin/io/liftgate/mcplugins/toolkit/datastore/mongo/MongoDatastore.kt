package io.liftgate.mcplugins.toolkit.datastore.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.Datastore
import io.liftgate.mcplugins.toolkit.export.Export
import io.liftgate.mcplugins.toolkit.runBlockingUnsafe
import jakarta.inject.Inject
import org.bson.UuidRepresentation
import org.glassfish.hk2.api.PostConstruct
import org.glassfish.hk2.api.PreDestroy
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.logging.Level
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
class MongoDatastore : PostConstruct, PreDestroy, Datastore<CoroutineDatabase>, Eager
{
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var config: MongoConfig.Model

    @Inject
    lateinit var plugin: ToolkitPlugin

    private lateinit var client: CoroutineClient
    private lateinit var database: CoroutineDatabase

    override fun postConstruct()
    {
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

        client = KMongo
            .createClient(
                clientSettings.build()
            )
            .coroutine

        database = client
            .getDatabase(config.database)

        logger.info("Loaded mongo services with database ${config.database}")
    }

    override fun preDestroy()
    {
        runBlockingUnsafe({
            logger.log(
                Level.SEVERE,
                "Failed to close Mongo datastore",
                it
            )
        }) {
            client.close()
            logger.info("Disposed of mongo services")
        }
    }

    override fun client() = this.database
}
