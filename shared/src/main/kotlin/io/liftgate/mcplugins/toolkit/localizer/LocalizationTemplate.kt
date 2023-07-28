package io.liftgate.mcplugins.toolkit.localizer

import org.jvnet.hk2.annotations.Contract
import kotlin.reflect.KClass

/**
 * Classifies an auto-registered localization
 * template.
 *
 * @author GrowlyX
 * @since 7/26/2023
 */
@Contract
interface LocalizationTemplate
{
    val langClass: KClass<*>
}
