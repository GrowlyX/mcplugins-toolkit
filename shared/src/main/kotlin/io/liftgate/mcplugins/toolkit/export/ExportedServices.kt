package io.liftgate.mcplugins.toolkit.export

import org.glassfish.hk2.api.ActiveDescriptor

/**
 * A collection of shared [Export]-marked services.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
object ExportedServices : MutableList<ActiveDescriptor<*>> by mutableListOf()
