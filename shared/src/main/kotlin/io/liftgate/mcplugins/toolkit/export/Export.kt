package io.liftgate.mcplugins.toolkit.export

import jakarta.inject.Qualifier

/**
 * Exposes the current local service to
 * all other ServiceLocators.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Qualifier
annotation class Export
