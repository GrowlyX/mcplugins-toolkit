package io.liftgate.mcplugins.toolkit.configuration

import kotlinx.serialization.KSerializer
import org.jvnet.hk2.annotations.Contract

/**
 * A contract marking yaml (kaml w/ kotlinx.serialization)
 * data-class-mapped configurations.
 *
 * @author GrowlyX
 * @since 6/1/2023
 */
@Contract
interface Configuration
{
    fun getFileName(): String
    fun serializer(): KSerializer<*>
}
