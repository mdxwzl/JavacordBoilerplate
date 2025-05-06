package tech.mdxwzl.contexts

import org.javacord.api.event.interaction.UserContextMenuCommandEvent
import tech.mdxwzl.annotations.LoadContextMenu
import tech.mdxwzl.enums.ContextMenuType
import tech.mdxwzl.interfaces.IUserContextMenu

@LoadContextMenu(
    name = "greet",
    type = ContextMenuType.USER,
)
class Greet: IUserContextMenu {
    override fun perform(event: UserContextMenuCommandEvent): Unit = with(event.userContextMenuInteraction) {
        log("Greet command executed by ${user.id} for ${target.id}")
        event.api.getUserById(user.id).thenAccept { user ->
            createImmediateResponder()
                .setContent("Howdy ${target.mentionTag}!")
                .respond()
        }
    }
}