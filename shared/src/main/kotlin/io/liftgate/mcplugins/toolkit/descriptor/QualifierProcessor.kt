package io.liftgate.mcplugins.toolkit.descriptor

import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.hk2.utilities.DescriptorImpl
import kotlin.reflect.KClass

/**
 * A wrapper around [DescriptorProcessor] which only
 * processes descriptors with a specific qualifier.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
abstract class QualifierProcessor(
    private val annotation: KClass<*>
) : DescriptorProcessor
{
    abstract fun process(descriptor: DescriptorImpl): DescriptorImpl

    override fun process(
        serviceLocator: ServiceLocator,
        descriptorImpl: DescriptorImpl
    ): DescriptorImpl
    {
        if (descriptorImpl.qualifiers.contains(annotation.java.name))
        {
            return process(descriptorImpl)
        }

        return descriptorImpl
    }
}
