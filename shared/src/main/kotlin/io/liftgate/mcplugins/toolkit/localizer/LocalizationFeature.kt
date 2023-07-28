package io.liftgate.mcplugins.toolkit.localizer

import io.liftgate.localize.Localizer
import io.liftgate.localize.buckets.YamlResourceBucket
import io.liftgate.mcplugins.toolkit.ToolkitPluginContainer
import io.liftgate.mcplugins.toolkit.feature.CorePluginFeature
import io.liftgate.mcplugins.toolkit.kompat.getAllServices
import org.jvnet.hk2.annotations.Service
import java.io.File

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
@Service
class LocalizationFeature : CorePluginFeature
{
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

                Localizer.of(it.langClass) { kClass ->
                    YamlResourceBucket(kClass, File(
                        languageDirectory,
                        "${kClass.java.simpleName
                            .removeSuffix("Lang")
                            .lowercase()}.yaml"
                    ))
                }
            }
    }
}
