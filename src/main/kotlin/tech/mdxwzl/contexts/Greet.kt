package tech.mdxwzl.contexts

import org.javacord.api.event.interaction.MessageContextMenuCommandEvent
import tech.mdxwzl.annotations.LoadContextMenu
import tech.mdxwzl.enums.ContextMenuType
import tech.mdxwzl.interfaces.IMessageContextMenu

@LoadContextMenu(
    name = "greet",
    type = ContextMenuType.MESSAGE,
)
class Greet: IMessageContextMenu {
    override fun perform(event: MessageContextMenuCommandEvent): Unit = with(event.messageContextMenuInteraction) {
        event.api.getUserById(user.id).thenAccept { user ->
            createImmediateResponder()
                .setContent("Howdy ${user.name}!")
                .respond()
        }
    }
}