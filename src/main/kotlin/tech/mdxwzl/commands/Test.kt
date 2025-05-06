package tech.mdxwzl.commands

import org.javacord.api.entity.permission.PermissionType
import org.javacord.api.event.interaction.SlashCommandCreateEvent
import tech.mdxwzl.annotations.LoadSlashCommand
import tech.mdxwzl.interfaces.ISlashCommand

@LoadSlashCommand("test", "Test slash command", PermissionType.ADMINISTRATOR)
class Test: ISlashCommand {
    override fun perform(event: SlashCommandCreateEvent): Unit = with(event.slashCommandInteraction){
        createImmediateResponder()
            .setContent("Test")
            .respond()
    }
}