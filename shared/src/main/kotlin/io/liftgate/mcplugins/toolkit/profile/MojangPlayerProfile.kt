package io.liftgate.mcplugins.toolkit.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API response from Mojang's sessionserver
 * and api endpoints.
 *
 * @author GrowlyX
 * @since 6/1/2023
 */
@Serializable
data class MojangPlayerProfile(
    val id: String,
    @SerialName("name")
    var username: String
)
