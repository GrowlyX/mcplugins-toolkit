package io.liftgate.mcplugins.toolkit.spigot.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayOutputStream
import kotlin.reflect.KClass

/**
 * https://github.com/jakobkmar/KSpigot
 */
inline fun <reified T : ConfigurationSerializable> build() =
    KSerializerForBukkit(T::class)

open class KSerializerForBukkit<T : ConfigurationSerializable>(
    private val kClass: KClass<T>,
) : KSerializer<T> {
    override val descriptor = ByteArraySerializer().descriptor

    override fun serialize(encoder: Encoder, value: T) {
        val bytes = ByteArrayOutputStream()
        BukkitObjectOutputStream(bytes).use {
            it.writeObject(value)
        }
        encoder.encodeSerializableValue(ByteArraySerializer(), bytes.toByteArray())
    }

    override fun deserialize(decoder: Decoder): T {
        BukkitObjectInputStream(
            decoder.decodeSerializableValue(ByteArraySerializer()).inputStream()
        ).use {
            @Suppress("UNCHECKED_CAST")
            return it.readObject() as? T
                ?: throw IllegalStateException("The object can not be deserialized to an object of the type ${kClass.simpleName}")
        }
    }
}
