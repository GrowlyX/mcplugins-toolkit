package io.liftgate.mcplugins.toolkit.platform.spigot.profile

import com.charleskorn.kaml.YamlComment
import io.liftgate.mcplugins.toolkit.configuration.Configuration
import kotlinx.serialization.Serializable
import org.jvnet.hk2.annotations.Service

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Service
class ProfileCachingConfig : Configuration<ProfileCachingConfig.Model>()
{
    @Serializable
    data class Model(
        @YamlComment(
            "This should only be enabled if you do not have your Spigot server proxied.",
            "If Toolkit is actively running on a proxy, it will handle profile caching",
            "and account duplication checks when a player logs on the proxy, therefore,",
            "keeping the functionality on Spigot sub-servers is useless."
        )
        val enabled: Boolean = true
    )

    override fun fileName() = "profile-caching.yml"
    override fun type() = Model::class
}
