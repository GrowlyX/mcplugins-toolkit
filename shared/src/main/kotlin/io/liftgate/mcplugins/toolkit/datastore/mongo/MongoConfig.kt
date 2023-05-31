package io.liftgate.mcplugins.toolkit.datastore.mongo

import jakarta.inject.Singleton

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Singleton // TODO: proper yaml config with kaml
data class MongoConfig(
    val uri: String = "mongodb://127.0.0.1:27017",
    val database: String = "experimental"
)
