package tech.mdxwzl.interfaces

import org.javacord.api.event.interaction.MessageContextMenuCommandEvent
import tech.mdxwzl.annotations.LoadContextMenu

interface IMessageContextMenu: IContextMenu {
    fun perform(event: MessageContextMenuCommandEvent)
}