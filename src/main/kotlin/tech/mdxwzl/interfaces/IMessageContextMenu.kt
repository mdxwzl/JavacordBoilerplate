package tech.mdxwzl.interfaces

import org.javacord.api.event.interaction.MessageContextMenuCommandEvent

interface IMessageContextMenu {
    fun perform(event: MessageContextMenuCommandEvent)
}