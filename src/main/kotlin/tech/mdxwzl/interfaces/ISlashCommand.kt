package tech.mdxwzl.interfaces

import org.javacord.api.event.interaction.SlashCommandCreateEvent
import tech.mdxwzl.annotations.LoadSlashCommand
import tech.mdxwzl.utils.BLUE
import tech.mdxwzl.utils.LogInfo
import tech.mdxwzl.utils.LogSuccess
import tech.mdxwzl.utils.WHITE

interface ISlashCommand {
    fun perform(event: SlashCommandCreateEvent)

    fun log(content: String) {
        val name = this::class.annotations
            .filterIsInstance<LoadSlashCommand>()
            .firstOrNull()
            ?.name ?: this::class.simpleName

        println("$LogInfo $BLUE[COMMAND - $name]$WHITE $content")
    }
}