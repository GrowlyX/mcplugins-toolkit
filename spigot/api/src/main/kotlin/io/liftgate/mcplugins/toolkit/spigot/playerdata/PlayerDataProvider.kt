package io.liftgate.mcplugins.toolkit.spigot.playerdata

import com.mongodb.client.ClientSession
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.mongo.MongoDatastore
import io.liftgate.mcplugins.toolkit.playerdata.PlayerData
import io.liftgate.mcplugins.toolkit.spigot.ToolkitSpigotPlugin
import jakarta.inject.Inject
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.glassfish.hk2.api.PostConstruct
import org.glassfish.hk2.api.ServiceLocator
import org.jvnet.hk2.annotations.Contract
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import org.litote.kmongo.util.KMongoUtil
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level

/**
 * A lifecycle manager for user data
 * for online players.
 *
 * Data is cached as long as the player
 * is logged onto the server.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
abstract class PlayerDataProvider<T : PlayerData> : Listener, PostConstruct, Eager
{
    private val playerProfileCache =
        ConcurrentHashMap<UUID, T>(1024)

    @Inject
    lateinit var plugin: ToolkitSpigotPlugin

    @Inject
    lateinit var mongo: MongoDatastore

    @Inject
    lateinit var serviceLocator: ServiceLocator

    lateinit var collection: MongoCollection<T>

    abstract fun createNew(uniqueId: UUID): T
    abstract fun typeOf(): Class<T>

    fun findNullable(uniqueId: UUID) = playerProfileCache[uniqueId]
    fun find(uniqueId: UUID) = playerProfileCache[uniqueId]!!

    fun findNullable(player: Player) = playerProfileCache[player.uniqueId]
    fun find(player: Player) = playerProfileCache[player.uniqueId]!!

    fun transactMulti(
        vararg uniqueIds: UUID,
        block: (T) -> Unit
    ) = CompletableFuture
        .supplyAsync(mongo::startSession)
        .thenAcceptAsync {
            val mapped = uniqueIds
                .map(::asyncLoadSingleProfile)
                .map(CompletableFuture<T>::join)
                .onEach {
                    block(it)
                }

            runCatching {
                it.startTransaction()
                for (model in mapped)
                {
                    collection.save(it, model)
                }

                it.commitTransaction()
                it.close()
            }.onFailure { _ ->
                it.close()
            }
        }

    fun transact(uniqueId: UUID, block: (T) -> Unit) = asyncLoadSingleProfile(uniqueId)
        .thenApply { block(it); it }
        .thenCompose(::save)

    private fun inject(profile: T) = profile.apply(serviceLocator::inject)

    fun asyncLoadSingleProfile(uniqueId: UUID) = CompletableFuture
        .supplyAsync { loadSingleProfile(uniqueId) }

    internal fun loadSingleProfileInSession(uniqueId: UUID, session: ClientSession) = inject(
        collection
            .findOne(
                session,
                Filters.eq(
                    "_id",
                    uniqueId.toString()
                )
            )
            ?: createNew(uniqueId)
    )

    internal fun loadSingleProfile(uniqueId: UUID) = inject(
        collection
            .findOne(
                Filters.eq(
                    "_id",
                    uniqueId.toString()
                )
            )
            ?: createNew(uniqueId)
    )

    override fun postConstruct()
    {
        collection = mongo.client().getCollection(
            KMongoUtil.defaultCollectionName(typeOf().kotlin),
            typeOf()
        )

        plugin.server.pluginManager
            .registerEvents(
                this, plugin
            )
    }

    @EventHandler
    fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent)
    {
        val playerProfile = collection
            .findOne(
                Filters.eq(
                    "_id",
                    event.uniqueId.toString()
                )
            )
            ?.apply {
                onLoadAsync()
                    .exceptionally {
                        plugin.logger.log(Level.WARNING, "Failed to run onLoad() for profile ${event.uniqueId}", it)
                        return@exceptionally null
                    }
                    .join()
            }
            ?: createNew(event.uniqueId)
                .apply {
                    CompletableFuture
                        .allOf(onInitialCreationAsync(), onLoadAsync())
                        .exceptionally {
                            plugin.logger.log(Level.WARNING, "Failed to run onLoad() for initial configuration of profile ${event.uniqueId}", it)
                            return@exceptionally null
                        }
                        .join()

                    save(profile = this)
                }

        playerProfileCache[event.uniqueId] = playerProfile
    }

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent)
    {
        findNullable(event.player)
            ?: return run {
                event.disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    "${ChatColor.RED}Your profile failed to load!"
                )
            }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent)
    {
        val profile = find(event.player)

        if (event.player.name.isNotEmpty())
        {
            val previousUsername = profile.username
            profile.username = event.player.name

            val usernameNoLongerMatches =
                previousUsername != profile.username

            if (usernameNoLongerMatches)
            {
                save(profile)
                plugin.logger.info(
                    "Pushing username update for ${profile.uniqueId}"
                )
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent)
    {
        val playerProfile = playerProfileCache
            .remove(event.player.uniqueId)
            ?.apply {
                onDestroy()
            }
            ?: return

        save(profile = playerProfile)
    }

    fun save(profile: T) = CompletableFuture
        .runAsync {
            collection.save(profile)
        }
}
