package tech.mdxwzl.interfaces

import org.javacord.api.event.interaction.UserContextMenuCommandEvent
import tech.mdxwzl.annotations.LoadContextMenu

interface IUserContextMenu: IContextMenu {
    fun perform(event: UserContextMenuCommandEvent){
    }
}