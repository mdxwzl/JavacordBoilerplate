package tech.mdxwzl

import io.github.cdimascio.dotenv.dotenv
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.server.Server
import org.javacord.api.listener.GloballyAttachableListener
import tech.mdxwzl.listeners.SlashCommandListener
import tech.mdxwzl.utils.LogSuccess
import org.reflections8.Reflections
import tech.mdxwzl.annotations.LoadContextMenu
import tech.mdxwzl.annotations.LoadListener
import tech.mdxwzl.annotations.LoadSlashCommand
import tech.mdxwzl.enums.ContextMenuType
import tech.mdxwzl.listeners.ContextMenuListener
import tech.mdxwzl.utils.LogError
import tech.mdxwzl.utils.LogInfo
import kotlin.jvm.optionals.getOrElse

class Client() {
    lateinit var discordApi: DiscordApi
    lateinit var server: Server
    lateinit var slashCommandListener: SlashCommandListener
    lateinit var contextMenuListener: ContextMenuListener

    init {
        instance = this
        login()
    }

    fun login(): DiscordApi {
        val token = dotenv().get("DISCORD_TOKEN") ?: error("DISCORD_TOKEN not found in .env")

        discordApi = DiscordApiBuilder().apply {
            setToken(token)
            setAllIntents()
        }.login().join()

        println("$LogSuccess Logged in as ${discordApi.yourself.discriminatedName}(${discordApi.yourself.id})")

        addListeners()

        val serverOverwrite = dotenv().get("SERVER_OVERWRITE") ?: ""
        if (serverOverwrite != "") {
            server = discordApi.getServerById(serverOverwrite).getOrElse {
                println("$LogError Server not found, using global commands")
                return discordApi
            }
        }

        return discordApi
    }

    fun logout() {
        discordApi.disconnect()
        println("$LogSuccess Successfully logged out")
    }

    private fun addListeners() {
        slashCommandListener = SlashCommandListener()
        contextMenuListener = ContextMenuListener()
    }

    fun registerSlashCommands(path: String) {
            val slashCommands = Reflections(path).getTypesAnnotatedWith(LoadSlashCommand::class.java)

        for (slashCommand in slashCommands) {
            slashCommandListener.addSlashCommand(slashCommand)
        }
        println("$LogSuccess Successfully loaded ${slashCommands.size} slash commands")
    }

    fun registerContextMenus(path: String) {
        val contextMenus = Reflections(path).getTypesAnnotatedWith(LoadContextMenu::class.java)

        for (context in contextMenus) {
            val type = context.getAnnotation(LoadContextMenu::class.java).type
            when (type) {
                ContextMenuType.USER -> contextMenuListener.addUserContextMenu(context)
                ContextMenuType.MESSAGE -> contextMenuListener.addMessageContextMenu(context)
                else -> println("$LogError Context Menu Type not found")
            }
        }
        println("$LogSuccess Successfully loaded ${contextMenus.size} context menus")
    }

    fun registerListeners(path: String) {
        val listeners = Reflections(path).getTypesAnnotatedWith(LoadListener::class.java)
        for (listener in listeners) {
            discordApi.addListener(listener.getDeclaredConstructor().newInstance() as GloballyAttachableListener)
            println("$LogInfo Registered Listener \"${listener.name.split(".").last()}\"")
        }

        println("$LogSuccess Successfully loaded ${listeners.size} Listeners")
    }

    companion object {
        lateinit var instance: Client
    }
}