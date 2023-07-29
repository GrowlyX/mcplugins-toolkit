package io.liftgate.mcplugins.toolkit.localizer

import io.liftgate.localize.Localizer
import io.liftgate.localize.buckets.YamlResourceBucket
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import io.liftgate.mcplugins.toolkit.utilities.FileWatcher
import io.liftgate.mcplugins.toolkit.utilities.FileWatcherObject
import jakarta.inject.Inject
import org.jvnet.hk2.annotations.Service
import java.io.File

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
@Service
class LocalizationFeature : CorePluginFeature
{
    @Inject
    lateinit var fileWatcher: FileWatcher

    override fun preEnable(plugin: ToolkitPluginContainer)
    {
        val languageDirectory = File(
            plugin.plugin.getDataFolder(),
            "language"
        )

        if (!languageDirectory.exists())
        {
            languageDirectory.mkdirs()
        }

        plugin.locator
            .getAllServices<LocalizationTemplate>()
            .forEach {
                plugin.plugin.getLogger().info(
                    "Loading localization resources for ${it.langClass.java.name}"
                )

                val file = File(
                    languageDirectory,
                    "${it.langClass.java.simpleName
                        .removeSuffix("Lang")
                        .lowercase()}.yaml"
                )

                val bucket = YamlResourceBucket(it.langClass, file)
                val createLocalizationTemplate = {
                    Localizer.registry.remove(it.langClass)
                    Localizer.of(it.langClass) { bucket }
                }

                fileWatcher.watch(
                    FileWatcherObject.builder()
                        .file(file)
                        .consumer { _ ->
                            createLocalizationTemplate()
                            plugin.plugin.getLogger().info(
                                "FileWatcher found changes to localization template and recreated it."
                            )
                        }
                        .build()
                )

                createLocalizationTemplate()
            }
    }
}
