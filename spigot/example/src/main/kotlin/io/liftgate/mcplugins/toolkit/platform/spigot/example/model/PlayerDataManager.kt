package io.liftgate.mcplugins.toolkit.platform.spigot.example.model

import io.liftgate.mcplugins.toolkit.datastore.mongo.MongoDatastore
import io.liftgate.mcplugins.toolkit.spigot.profiles.PlayerDataProvider
import jakarta.inject.Inject
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.jvnet.hk2.annotations.Service
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.aggregate
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

    private lateinit var collection: CoroutineCollection<PlayerDataModel>

    override fun collection() = collection
    override fun createNew(uniqueId: UUID) = PlayerDataModel(uniqueId, "")

    override fun postConstruct()
    {
        collection = mongo.client()
            .getCollection<PlayerDataModel>()
        super.postConstruct()
    }

    @EventHandler
    suspend fun onPlayerDeath(event: PlayerDeathEvent)
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

    suspend fun aggregateTop10KillsEntries() =
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
