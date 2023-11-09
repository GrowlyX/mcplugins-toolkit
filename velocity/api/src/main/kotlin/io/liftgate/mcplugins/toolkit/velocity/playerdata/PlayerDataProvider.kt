package io.liftgate.mcplugins.toolkit.velocity.playerdata

import com.mongodb.client.ClientSession
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoIterable
import com.mongodb.client.model.Filters
import com.velocitypowered.api.event.EventTask.async
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.proxy.Player
import io.liftgate.mcplugins.toolkit.contracts.Eager
import io.liftgate.mcplugins.toolkit.datastore.mongo.MongoDatastore
import io.liftgate.mcplugins.toolkit.playerdata.PlayerData
import io.liftgate.mcplugins.toolkit.velocity.ToolkitVelocityPlugin
import io.liftgate.mcplugins.toolkit.velocity.listeners.ToolkitListener
import jakarta.inject.Inject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.glassfish.hk2.api.PostConstruct
import org.glassfish.hk2.api.ServiceLocator
import org.jvnet.hk2.annotations.Contract
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import org.litote.kmongo.util.KMongoUtil
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

/**
 * Lifecycle management for online-player user data. Models
 * are cached as long as the player is logged onto the server.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
abstract class PlayerDataProvider<T : PlayerData> : ToolkitListener, PostConstruct, Eager
{
    private val playerProfileCache =
        ConcurrentHashMap<UUID, T>(1024)

    @Inject
    lateinit var plugin: ToolkitVelocityPlugin

    @Inject
    lateinit var mongo: MongoDatastore

    @Inject
    lateinit var serviceLocator: ServiceLocator

    lateinit var collection: MongoCollection<T>
    private var readOnlyCopies = false

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

            if (readOnlyCopies)
            {
                return@thenAcceptAsync
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

    fun setReadOnly()
    {
        readOnlyCopies = true
    }

    override fun postConstruct()
    {
        collection = mongo.client().getCollection(
            KMongoUtil.defaultCollectionName(typeOf().kotlin),
            typeOf()
        )

        plugin.server.eventManager
            .register(
                plugin, this
            )
    }

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

    @Subscribe
    fun onPlayerPreLogin(event: LoginEvent)
    {
        runCatching {
            val playerProfile = loadSingleProfile(event.player.uniqueId)
            playerProfileCache[event.player.uniqueId] = playerProfile

            val previousUsername = playerProfile.username
            playerProfile.username = event.player.username

            val usernameNoLongerMatches =
                previousUsername != playerProfile.username

            if (usernameNoLongerMatches)
            {
                save(playerProfile)
                plugin._logger.info(
                    "Pushing username update for ${playerProfile.uniqueId}"
                )
            }
        }.onFailure {
            event.result = ResultedEvent.ComponentResult.denied(
                Component
                    .text("Your profile failed to load!")
                    .color(NamedTextColor.RED)
            )
        }
    }

    @Subscribe
    fun onPlayerQuit(event: DisconnectEvent)
    {
        val playerProfile = playerProfileCache
            .remove(event.player.uniqueId)
            ?: return
        save(profile = playerProfile)
    }

    fun save(profile: T): CompletableFuture<Void>
    {
        if (readOnlyCopies)
        {
            return CompletableFuture.completedFuture(null)
        }

        return CompletableFuture.runAsync {
            collection.save(profile)
        }
    }
}
