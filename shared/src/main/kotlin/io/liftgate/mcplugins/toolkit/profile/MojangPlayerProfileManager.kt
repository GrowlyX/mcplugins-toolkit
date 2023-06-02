package io.liftgate.mcplugins.toolkit.profile

import com.mongodb.client.model.IndexOptions
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.mongo.MongoDatastore
import io.liftgate.mcplugins.toolkit.export.Export
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.coroutine.CoroutineCollection
import java.util.logging.Logger

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Export
@Service
class MojangPlayerProfileManager : Eager, PostConstruct
{
    @Inject
    lateinit var mongo: MongoDatastore

    @Inject
    lateinit var logger: Logger

    private lateinit var collection: CoroutineCollection<MojangPlayerProfile>

    override fun postConstruct()
    {
        collection = mongo.client()
            .getCollection<MojangPlayerProfile>()

        runBlocking {
            collection.createIndex(
                "_id",
                IndexOptions().unique(true)
            )
            collection.createIndex("username")

            logger.info(
                "Created MojangPlayerProfileManager collection indexes"
            )
        }
    }
}
