package tech.mdxwzl.commands

import org.javacord.api.event.interaction.SlashCommandCreateEvent
import org.javacord.api.interaction.SlashCommandOption
import tech.mdxwzl.annotations.LoadSlashCommand
import tech.mdxwzl.interfaces.HasOptions

@LoadSlashCommand(
    name = "repeat",
    description = "Repeat the input string",
    options = true
)
class Repeat: HasOptions {
    override fun perform(event: SlashCommandCreateEvent): Unit = with(event.slashCommandInteraction){
        createImmediateResponder()
            .setContent(options[0].stringValue.get())
            .respond()
    }

    override fun getOptions(): List<SlashCommandOption> {
        return listOf(
            SlashCommandOption.createStringOption("input", "This is a string option", true),
        )
    }
}