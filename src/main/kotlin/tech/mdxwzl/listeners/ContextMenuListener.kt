package tech.mdxwzl.listeners

import org.javacord.api.event.interaction.MessageContextMenuCommandEvent
import org.javacord.api.event.interaction.UserContextMenuCommandEvent
import org.javacord.api.interaction.MessageContextMenuBuilder
import org.javacord.api.interaction.UserContextMenuBuilder
import org.javacord.api.listener.interaction.MessageContextMenuCommandListener
import org.javacord.api.listener.interaction.UserContextMenuCommandListener
import tech.mdxwzl.Client
import tech.mdxwzl.annotations.ContextMenuType
import tech.mdxwzl.annotations.LoadContextMenu
import tech.mdxwzl.annotations.LoadSlashCommand
import tech.mdxwzl.interfaces.IMessageContextMenu
import tech.mdxwzl.interfaces.IUserContextMenu
import tech.mdxwzl.utils.LogError
import tech.mdxwzl.utils.LogSuccess

class ContextMenuListener: MessageContextMenuCommandListener, UserContextMenuCommandListener {
    val discordApi = Client.instance.discordApi
    private val messageContextMenus = mutableMapOf<String, IMessageContextMenu>()
    private val userContextMenus = mutableMapOf<String, IUserContextMenu>()

    init {
        discordApi.addMessageContextMenuCommandListener(this)
        discordApi.addUserContextMenuCommandListener(this)
        println("$LogSuccess Listening for User & Message Context Menus")
    }

    fun addMessageContextMenu(handler: Class<*>) {
        val annotation = handler.getAnnotation(LoadContextMenu::class.java)
        val name = annotation.name
        val type = annotation.type
        val permission = annotation.permission
        val enabledInDms = annotation.enabledInDms

        val builder = MessageContextMenuBuilder()
            .setName(name)
            .setDefaultEnabledForPermissions(permission)
            .setEnabledInDms(enabledInDms)

        val contextMenu = handler.getConstructor().newInstance() as IMessageContextMenu

        messageContextMenus[name] = contextMenu

        if (Client.instance.serverOverwrite == "") {
            builder.createGlobal(discordApi).join()
            println("$LogSuccess Registered Message Context Menu \"$name\" (Global)")
        }else {
            val server = discordApi.getServerById(Client.instance.serverOverwrite).orElse(null)

            if (server == null) {
                println("$LogError Server(${Client.instance.serverOverwrite}) not found")
                return
            }

            builder.createForServer(server).join()
            println("$LogSuccess Registered Message Context Menu \"$name\" (${server.name})")
        }

    }

    fun addUserContextMenu(handler: Class<*>) {
        val annotation = handler.getAnnotation(LoadContextMenu::class.java)
        val name = annotation.name
        val permission = annotation.permission
        val enabledInDms = annotation.enabledInDms

        val builder = UserContextMenuBuilder()
            .setName(name)
            .setDefaultEnabledForPermissions(permission)
            .setEnabledInDms(enabledInDms)

        val contextMenu = handler.getConstructor().newInstance() as IUserContextMenu

        userContextMenus[name] = contextMenu

        if (Client.instance.serverOverwrite == "") {
            builder.createGlobal(discordApi).join()
            println("$LogSuccess Registered User Context Menu \"$name\" (Global)")
        }else {
            val server = discordApi.getServerById(Client.instance.serverOverwrite).orElse(null)

            if (server == null) {
                println("$LogError Server(${Client.instance.serverOverwrite}) not found")
                return
            }

            builder.createForServer(server).join()
            println("$LogSuccess Registered User Context Menu \"$name\" (${server.name})")
        }
    }



    override fun onMessageContextMenuCommand(event: MessageContextMenuCommandEvent): Unit = with(event.messageContextMenuInteraction){
        val contextMenu = messageContextMenus[commandName] ?: return

        // just for safety, discord should handle this on its own
        val permission = contextMenu::class.java.getAnnotation(LoadContextMenu::class.java).permission
        val server = event.api.getServerById(server.get().id).get()
        if (!server.hasPermission(user, permission)) {
            return
        }

        contextMenu.perform(event)
    }

    override fun onUserContextMenuCommand(event: UserContextMenuCommandEvent): Unit = with(event.userContextMenuInteraction){
        val contextMenu = userContextMenus[commandName] ?: return

        // just for safety, discord should handle this on its own
        val permission = contextMenu::class.java.getAnnotation(LoadContextMenu::class.java).permission
        val server = event.api.getServerById(server.get().id).get()
        if (!server.hasPermission(user, permission)) {
            return
        }

        contextMenu.perform(event)
    }
}