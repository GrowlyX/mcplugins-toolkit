package io.liftgate.mcplugins.toolkit.datastore.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import io.liftgate.mcplugins.toolkit.datastore.Datastore
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
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class MongoDatastoreService : PostConstruct, PreDestroy, Datastore<CoroutineDatabase>
{
    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var config: MongoConfig

    private lateinit var client: CoroutineClient
    private lateinit var database: CoroutineDatabase

    override fun postConstruct()
    {
        client = KMongo
            .createClient(
                MongoClientSettings
                    .builder()
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .applyConnectionString(
                        ConnectionString(config.uri)
                    )
                    .build()
            )
            .coroutine

        database = client.getDatabase(config.database)
        logger.info("Loaded mongo using database ${config.database}")
    }

    override fun preDestroy()
    {
        runCatching {
            client.close()
        }.onFailure {
            logger.log(
                Level.SEVERE,
                "Failed to close Mongo datastore",
                it
            )
        }
    }

    override fun client() = this.database
}
