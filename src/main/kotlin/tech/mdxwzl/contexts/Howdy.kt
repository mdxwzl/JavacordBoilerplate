package tech.mdxwzl.contexts

import org.javacord.api.event.interaction.MessageContextMenuCommandEvent
import tech.mdxwzl.annotations.ContextMenuType
import tech.mdxwzl.annotations.LoadContextMenu
import tech.mdxwzl.interfaces.IMessageContextMenu

@LoadContextMenu(
    "howdy",
    ContextMenuType.MESSAGE
)
class Howdy: IMessageContextMenu {
    override fun perform(event: MessageContextMenuCommandEvent): Unit = with(event.messageContextMenuInteraction) {
        event.api.getUserById(user.id).thenAccept { user ->
            createImmediateResponder()
                .setContent("Howdy ${user.name}")
                .respond()
        }
    }
}