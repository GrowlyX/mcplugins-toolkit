package io.liftgate.mcplugins.toolkit.datastore

import org.jvnet.hk2.annotations.Contract

/**
 * A contract marking disposable data storage clients. 
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
interface Datastore<C>
{
    fun client(): C
}
