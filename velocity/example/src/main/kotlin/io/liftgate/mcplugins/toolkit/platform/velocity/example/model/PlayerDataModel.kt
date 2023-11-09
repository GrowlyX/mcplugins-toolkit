package io.liftgate.mcplugins.toolkit.platform.velocity.example.model

import io.liftgate.mcplugins.toolkit.playerdata.PlayerData
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Serializable
data class PlayerDataModel(
    @SerialName("_id")
    override val uniqueId: @Contextual UUID,
    override var username: String = "",
    var deaths: Int = 0
) : PlayerData
