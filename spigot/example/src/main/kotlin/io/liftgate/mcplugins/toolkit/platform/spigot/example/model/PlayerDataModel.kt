package io.liftgate.mcplugins.toolkit.platform.spigot.example.model

import io.liftgate.mcplugins.toolkit.spigot.profiles.PlayerData
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Serializable
data class PlayerDataModel(
    @BsonId
    override val uniqueId: @Contextual UUID,
    override var username: String,
    var deaths: Int = 0
) : PlayerData
