package io.liftgate.mcplugins.toolkit.spigot.version;

import org.glassfish.hk2.api.Metadata;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author GrowlyX
 * @since 6/3/2023
 */
@Target(ElementType.TYPE)
public @interface RequiresVersion {
    @NotNull
    @Metadata("ServerVersion")
    String value();
}
