package io.liftgate.mcplugins.toolkit.datastore.mongo

import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service // TODO: proper yaml config with kaml
data class MongoConfig(
    val uri: String = "mongodb://127.0.0.1:27017",
    val database: String = "experimental"
)
