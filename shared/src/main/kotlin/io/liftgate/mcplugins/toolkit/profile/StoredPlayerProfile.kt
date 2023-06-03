package io.liftgate.mcplugins.toolkit.profile

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Since player IDs are returned in Mojang's format when
 * grabbed from the API, we map it to the standard UUID
 * form in our own mongo data model.
 *
 * Prevents the need for continuous UUID parsing or weird data
 * loading with the REST client.
 *
 * @author GrowlyX
 * @since 6/1/2023
 */
@Serializable
data class StoredPlayerProfile(
    @SerialName("_id")
    val uniqueId: @Contextual UUID,
    var username: String
)
