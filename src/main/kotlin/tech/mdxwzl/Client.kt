package tech.mdxwzl

import io.github.cdimascio.dotenv.dotenv
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.interaction.ApplicationCommandBuilder
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

class Client() {
    val token = dotenv().get("DISCORD_TOKEN") ?: error("DISCORD_TOKEN not found in .env")
    val serverOverwrite = dotenv().get("SERVER_OVERWRITE")
    lateinit var discordApi: DiscordApi
    lateinit var slashCommandListener: SlashCommandListener
    lateinit var contextMenuListener: ContextMenuListener

    init {
        instance = this
        login(token)
    }

    fun login(token: String): DiscordApi {
        discordApi = DiscordApiBuilder().apply {
            setToken(token)
            setAllIntents()
        }.login().join()

        println("$LogSuccess Logged in as ${discordApi.yourself.discriminatedName}(${discordApi.yourself.id})")

        addListeners()

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
    }

    fun registerListeners(path: String) {
        val listeners = Reflections(path).getTypesAnnotatedWith(LoadListener::class.java)
        for (listener in listeners) {
            discordApi.addListener(listener.getDeclaredConstructor().newInstance() as GloballyAttachableListener)
            println("${LogSuccess} Registered Listener \"${listener.name.split(".").last()}\"")
        }

        println("$LogSuccess Successfully loaded ${listeners.size} Listeners")
    }

    companion object {
        lateinit var instance: Client
    }
}