package io.liftgate.mcplugins.toolkit.configuration

import com.charleskorn.kaml.Yaml
import io.liftgate.mcplugins.toolkit.ToolkitPlugin
import jakarta.inject.Inject
import kotlinx.serialization.serializer
import org.glassfish.hk2.api.ServiceLocator
import org.jvnet.hk2.annotations.Contract
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.KClass

/**
 * A contract housing config-specific metadata. Type [T] is the
 * [kotlinx.serialization.Serializable] config model.
 *
 * The [yaml] instance is pre-configured with the same [SerializersModule] as KMongo.
 * Custom plugin serializers are also applied as [SerializationFeature] is
 * configured prior to the creation of the config model.
 *
 * By default, [createNewOf] uses the injected [ServiceLocator] to create
 * and initialize a new instance of the config model. The locator assumes
 * that there is a default no-args constructor present in the model.
 *
 * @author GrowlyX
 * @since 6/1/2023
 */
@Contract
abstract class Configuration<T : Any>
{
    abstract fun fileName(): String
    abstract fun type(): KClass<*>

    private lateinit var file: File

    @Inject
    lateinit var yaml: Yaml

    @Inject
    lateinit var locator: ServiceLocator

    @Inject
    fun injectPlugin(plugin: ToolkitPlugin)
    {
        file = File(
            plugin.getDataFolder(),
            fileName()
        )

        if (!file.exists())
        {
            save(createNewOf())
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun createNewOf() =
        locator.createAndInitialize(type().java) as T

    fun load() = file.inputStream()
        .use {
            decode(it)
        }

    fun save(model: T)
    {
        file.outputStream()
            .use {
                encode(it, model)
            }
    }

    fun decode(
        inputStream: InputStream
    ): T
    {
        return yaml
            .decodeFromStream(
                serializer(type().java),
                inputStream
            ) as T
    }

    fun encode(
        outputStream: OutputStream,
        `object`: T = createNewOf()
    )
    {
        yaml.encodeToStream(
            serializer(`object`.javaClass),
            `object`,
            outputStream
        )
    }
}
