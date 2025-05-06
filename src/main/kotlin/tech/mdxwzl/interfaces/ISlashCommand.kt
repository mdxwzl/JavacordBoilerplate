package tech.mdxwzl.interfaces

import org.javacord.api.event.interaction.SlashCommandCreateEvent

interface ISlashCommand {
    fun perform(event: SlashCommandCreateEvent)
}