package io.liftgate.mcplugins.toolkit.datastore

import org.jvnet.hk2.annotations.Contract

/**
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
interface Datastore<C>
{
    fun client(): C
}
