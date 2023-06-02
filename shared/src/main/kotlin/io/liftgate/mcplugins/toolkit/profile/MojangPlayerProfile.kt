package io.liftgate.mcplugins.toolkit.profile

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

/**
 * @author GrowlyX
 * @since 6/1/2023
 */
@Serializable
data class MojangPlayerProfile(
    @BsonId
    val uniqueId: @Contextual UUID,
    var username: String
)
