package io.liftgate.mcplugins.toolkit.commands

import co.aikar.commands.BaseCommand
import org.jvnet.hk2.annotations.Contract

/**
 * Contract used for component scan of BaseCommands.
 *
 * @author GrowlyX
 * @since 5/31/2023
 */
@Contract
abstract class ToolkitCommand : BaseCommand()
