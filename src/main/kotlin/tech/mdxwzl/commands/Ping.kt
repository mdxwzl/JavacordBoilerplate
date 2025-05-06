package tech.mdxwzl.commands

import org.javacord.api.event.interaction.SlashCommandCreateEvent
import tech.mdxwzl.annotations.LoadSlashCommand
import tech.mdxwzl.interfaces.ISlashCommand

@LoadSlashCommand(
    name = "ping",
    description = "Check if the bot is alive"
)
class Ping: ISlashCommand {
    override fun perform(event: SlashCommandCreateEvent): Unit = with(event.slashCommandInteraction){
        log("Ping command executed by ${user.id}")
        createImmediateResponder()
            .setContent("Pong!")
            .respond()
    }
}