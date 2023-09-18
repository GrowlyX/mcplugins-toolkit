package io.liftgate.mcplugins.toolkit.profile

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Collation
import com.mongodb.client.model.IndexOptions
import io.github.reactivecircus.cache4k.Cache
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.mongo.MongoDatastore
import io.liftgate.mcplugins.toolkit.datastore.restful.RESTDatastore
import io.liftgate.mcplugins.toolkit.export.Export
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.glassfish.hk2.api.PostConstruct
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.*
import java.util.*
import java.util.concurrent.CompletableFuture.runAsync
import java.util.logging.Logger
import kotlin.time.Duration.Companion.minutes

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

        @JvmStatic
        val LOCAL_EXPIRATION_DURATION = 10L.minutes

        const val MOJANG_USERNAME_ENDPOINT = "https://api.mojang.com/users/profiles/minecraft/%s"
        const val MOJANG_UNIQUEID_ENDPOINT = "https://sessionserver.mojang.com/session/minecraft/profile/%s"
    }

    @Inject
    lateinit var mongo: MongoDatastore

    @Inject
    lateinit var rest: RESTDatastore

    @Inject
    lateinit var logger: Logger

    private lateinit var collection: MongoCollection<StoredPlayerProfile>

    private val idToProfileMappings = Cache.Builder<UUID, StoredPlayerProfile>()
        .expireAfterWrite(LOCAL_EXPIRATION_DURATION)
        .build()

    private val nameToProfileMappings = Cache.Builder<String, StoredPlayerProfile>()
        .expireAfterWrite(LOCAL_EXPIRATION_DURATION)
        .build()

    override fun postConstruct()
    {
        collection = mongo.client()
            .getCollection<StoredPlayerProfile>()

        runBlocking {
            collection.createIndex(
                ascending(
                    StoredPlayerProfile::username
                ),
                IndexOptions().collation(
                    Collation.builder()
                        .locale("en_US")
                        .caseLevel(false)
                        .build()
                )
            )

            logger.info(
                "Created StoredPlayerProfileManager collection indexes"
            )
        }
    }

    fun findDuplicates(username: String, id: UUID) =
        collection
            .find(
                and(
                    StoredPlayerProfile::username eq username,
                    StoredPlayerProfile::uniqueId ne id
                )
            )
            .toList()

    fun cacheStoredProfile(storedProfile: StoredPlayerProfile)
    {
        populateLocalCaches(storedProfile)
        collection.save(storedProfile)
    }

    fun loadProfileFromUniqueId(uniqueId: UUID) =
        loadProfileFromUniqueIdNullable(uniqueId)!!

    fun loadProfileFromUniqueIdNullable(uniqueId: UUID) = runAsync {
        idToProfileMappings.get(uniqueId)
            ?: collection
                .findOne(
                    StoredPlayerProfile::uniqueId eq uniqueId
                )
                ?.apply {
                    populateLocalCaches(this)
                }
            ?: parseAndCacheAPILoadedProfile(
                loadAPIProfileFromUniqueId(uniqueId)
            )
    }

    fun loadProfileFromUsername(username: String) =
        loadProfileFromUsernameNullable(username)!!

    fun loadProfileFromUsernameNullable(username: String) = runAsync {
        nameToProfileMappings.get(username.lowercase())
            ?: collection
                .findOne(
                    StoredPlayerProfile::username eq username
                )
                ?.apply {
                    populateLocalCaches(this)
                }
            ?: parseAndCacheAPILoadedProfile(
                loadAPIProfileFromUsername(username)
            )
    }

    private fun populateLocalCaches(profile: StoredPlayerProfile)
    {
        idToProfileMappings.put(profile.uniqueId, profile)
        nameToProfileMappings.put(profile.username.lowercase(), profile)
    }

    private fun parseAndCacheAPILoadedProfile(
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

        populateLocalCaches(storedProfile)
        collection.save(storedProfile)

        return storedProfile
    }

    fun loadAPIProfileFromUsername(username: String) = runBlocking {
        rest.client()
            .get(
                MOJANG_USERNAME_ENDPOINT.format(username)
            )
            .body<MojangPlayerProfile?>()
    }

    fun loadAPIProfileFromUniqueId(uniqueId: UUID) = runBlocking {
        rest.client()
            .get(
                MOJANG_UNIQUEID_ENDPOINT.format(uniqueId)
            )
            .body<MojangPlayerProfile?>()
    }
}
