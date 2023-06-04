package io.liftgate.mcplugins.toolkit.velocity.listeners

import org.jvnet.hk2.annotations.Contract

/**
 * A contract marking a service that holds suspending event
 * handlers. [CoroutineListener]s are eagerly loaded, similar
 * to what the [Eager] interface provides.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
interface CoroutineListener
