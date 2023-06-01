package io.liftgate.mcplugins.toolkit.export

import org.jvnet.hk2.annotations.Contract
import jakarta.inject.Qualifier;

/**
 * Exposed a plugin-local service to other plugins. Exported services
 * also stay local, and are also used as a service locally.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Qualifier
annotation class Export
