package io.liftgate.mcplugins.toolkit.platform.spigot.example.model

import com.mongodb.client.MongoCollection
import io.liftgate.localize.Localizer
import io.liftgate.mcplugins.toolkit.datastore.mongo.MongoDatastore
import io.liftgate.mcplugins.toolkit.platform.spigot.example.localizer.CoreLang
import io.liftgate.mcplugins.toolkit.spigot.playerdata.PlayerDataProvider
import jakarta.inject.Inject
import kotlinx.coroutines.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.*
import java.util.*

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class PlayerDataManager : PlayerDataProvider<PlayerDataModel>()
{
    @Inject
    lateinit var mongo: MongoDatastore

    private lateinit var collection: MongoCollection<PlayerDataModel>

    override fun collection() = collection
    override fun createNew(uniqueId: UUID) = PlayerDataModel(uniqueId, "")

    override fun postConstruct()
    {
        collection = mongo.client()
            .getCollection<PlayerDataModel>()
        super.postConstruct()
    }

    @EventHandler
    fun PlayerJoinEvent.on()
    {
        joinMessage = Localizer.of<CoreLang>()
            .playerJoins(player)
            .first()
    }

    @EventHandler
    fun PlayerQuitEvent.on()
    {
        quitMessage = Localizer.of<CoreLang>()
            .playerLeaves(player, "quit")
            .first()
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent)
    {
        findNullable(event.entity)
            ?.apply {
                this.deaths += 1
                save(this)
            }
    }

    @Serializable
    data class Top10Result(
        val uniqueId: @Contextual UUID,
        val username: String,
        val value: Int
    )

    fun aggregateTop10KillsEntries() =
        collection
            .aggregate<Top10Result>(
                sort(
                    descending(PlayerDataModel::deaths)
                ),
                limit(10),
                project(
                    Top10Result::uniqueId from PlayerDataModel::uniqueId,
                    Top10Result::username from PlayerDataModel::username,
                    Top10Result::value from PlayerDataModel::deaths
                )
            )
            .toList()
}
