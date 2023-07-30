package io.liftgate.mcplugins.toolkit.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.overwriteWith
import org.litote.kmongo.serialization.kmongoSerializationModule
import java.time.Instant
import java.util.*

private val defaultSerializationModule = SerializersModule {
    contextual(UUID::class, UUIDSerializer)
    contextual(Instant::class, InstantSerializer)
}

val strippedKMongoSerializationModule: SerializersModule
    get()
    {
        return kmongoSerializationModule.overwriteWith(
            defaultSerializationModule
        )
    }

object UUIDSerializer : KSerializer<UUID>
{
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUIDSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID =
        UUID.fromString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: UUID)
    {
        encoder.encodeString(value.toString())
    }
}

object InstantSerializer : KSerializer<Instant>
{
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("InstantSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant =
        Instant.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Instant)
    {
        encoder.encodeString(value.toString())
    }
}
