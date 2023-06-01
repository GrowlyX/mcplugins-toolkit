package io.liftgate.mcplugins.toolkit.descriptor

import org.glassfish.hk2.api.PopulatorPostProcessor
import org.jvnet.hk2.annotations.Contract

/**
 * Wrapper around [PopulatorPostProcessor] allowing for additional
 * metadata to be added to loaded descriptors.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
interface DescriptorProcessor : PopulatorPostProcessor
