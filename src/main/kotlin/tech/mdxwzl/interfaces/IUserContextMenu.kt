package tech.mdxwzl.interfaces

import org.javacord.api.event.interaction.UserContextMenuCommandEvent

interface IUserContextMenu {
    fun perform(event: UserContextMenuCommandEvent){
    }
}