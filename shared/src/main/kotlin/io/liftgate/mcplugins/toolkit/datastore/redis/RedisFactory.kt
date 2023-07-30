package io.liftgate.mcplugins.toolkit.datastore.redis

import gg.scala.aware.AwareBuilder
import gg.scala.aware.codec.codecs.JsonRedisCodec
import gg.scala.aware.codec.codecs.interpretation.AwareMessageCodec
import gg.scala.aware.message.Message
import io.lettuce.core.api.StatefulRedisConnection
import java.util.logging.Logger

/**
 * @author GrowlyX
 * @since 7/29/2023
 */
class RedisFactory internal constructor(
    private val localKVConnection: StatefulRedisConnection<String, String>,
    val logger: Logger
)
{
    fun cache() = localKVConnection

    fun buildNewMessageConnection(channel: String) = AwareBuilder
        .of<Message>(channel)
        .logger(logger)
        .codec(AwareMessageCodec)
        .build()

    inline fun <reified T : Any> buildNewCustomConnection(
        channel: String, noinline packetGetter: (T) -> String
    ) = AwareBuilder
        .of<T>(channel)
        .logger(logger)
        .codec(JsonRedisCodec.of(packetGetter))
        .build()
}
