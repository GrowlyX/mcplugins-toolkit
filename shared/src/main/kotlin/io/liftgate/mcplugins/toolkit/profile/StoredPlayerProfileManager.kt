package io.liftgate.mcplugins.toolkit.profile

import com.mongodb.client.model.IndexOptions
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.mongo.MongoDatastore
import io.liftgate.mcplugins.toolkit.export.Export
import io.liftgate.mcplugins.toolkit.datastore.restful.RESTDatastore
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import java.util.UUID
import java.util.logging.Logger

/**
 * Caching mechanism for Mojang player account
 * UUID/username pairs in Mongo.
 *
 * @author GrowlyX
 * @since 6/1/2023
 */
@Export
@Service
class StoredPlayerProfileManager : Eager, PostConstruct
{
    companion object
    {
        @JvmStatic
        val MOJANG_ID_REGEX = "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex()

        const val MOJANG_USERNAME_ENDPOINT = "https://api.mojang.com/users/profiles/minecraft/%s"
        const val MOJANG_UNIQUEID_ENDPOINT = "https://sessionserver.mojang.com/session/minecraft/profile/%s"
    }

    @Inject
    lateinit var mongo: MongoDatastore

    @Inject
    lateinit var rest: RESTDatastore

    @Inject
    lateinit var logger: Logger

    private lateinit var collection: CoroutineCollection<StoredPlayerProfile>
    // TODO: local 10m caching

    override fun postConstruct()
    {
        collection = mongo.client()
            .getCollection<StoredPlayerProfile>()

        runBlocking {
            collection.createIndex(
                "_id",
                IndexOptions().unique(true)
            )
            collection.createIndex("username")

            logger.info(
                "Created StoredPlayerProfileManager collection indexes"
            )
        }
    }

    suspend fun loadProfileFromUniqueId(uniqueId: UUID) =
        loadProfileFromUniqueIdNullable(uniqueId)!!

    suspend fun loadProfileFromUniqueIdNullable(uniqueId: UUID) =
        collection
            .findOne(
                StoredPlayerProfile::uniqueId eq uniqueId
            )
            ?: parseAndCacheAPILoadedProfile(
                loadAPIProfileFromUniqueId(uniqueId)
            )

    suspend fun loadProfileFromUsername(username: String) =
        loadProfileFromUsernameNullable(username)!!

    suspend fun loadProfileFromUsernameNullable(username: String) =
        collection
            .findOne(
                // TODO: case insensitive or not?
                StoredPlayerProfile::username eq username
            )
            ?: parseAndCacheAPILoadedProfile(
                loadAPIProfileFromUsername(username)
            )

    private suspend fun parseAndCacheAPILoadedProfile(
        apiLoaded: MojangPlayerProfile?
    ): StoredPlayerProfile?
    {
        if (apiLoaded == null)
            return null

        val formatted = apiLoaded.id
            .replaceFirst(
                MOJANG_ID_REGEX,
                "$1-$2-$3-$4-$5"
            )

        val storedProfile = StoredPlayerProfile(
            uniqueId = UUID.fromString(formatted),
            username = apiLoaded.username
        )

        collection.save(storedProfile)
        return storedProfile
    }

    suspend fun loadAPIProfileFromUsername(username: String) =
        rest.client()
            .get(
                MOJANG_USERNAME_ENDPOINT.format(username)
            )
            .body<MojangPlayerProfile?>()

    suspend fun loadAPIProfileFromUniqueId(uniqueId: UUID) =
        rest.client()
            .get(
                MOJANG_UNIQUEID_ENDPOINT.format(uniqueId)
            )
            .body<MojangPlayerProfile?>()
}
