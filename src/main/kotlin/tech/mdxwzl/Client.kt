package tech.mdxwzl

import io.github.cdimascio.dotenv.dotenv
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.interaction.ApplicationCommandBuilder
import org.javacord.api.listener.GloballyAttachableListener
import tech.mdxwzl.listeners.SlashCommandListener
import tech.mdxwzl.utils.LogSuccess
import org.reflections8.Reflections
import tech.mdxwzl.annotations.ContextMenuType
import tech.mdxwzl.annotations.LoadContextMenu
import tech.mdxwzl.annotations.LoadListener
import tech.mdxwzl.annotations.LoadSlashCommand
import tech.mdxwzl.listeners.ContextMenuListener
import tech.mdxwzl.utils.LogError

class Client(token: String) {
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

    private fun addListeners() {
        slashCommandListener = SlashCommandListener()
        contextMenuListener = ContextMenuListener()
    }

    fun registerSlashCommands(path: String) {
        val slashCommands = Reflections(path).getTypesAnnotatedWith(LoadSlashCommand::class.java)

        for (handler in slashCommands) {
            try {
                slashCommandListener.addSlashCommand(handler)
            } catch (exception: InstantiationException) {
                exception.printStackTrace()
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            }
        }
    }

    fun registerContextMenus(path: String) {
        val contextMenus = Reflections(path).getTypesAnnotatedWith(LoadContextMenu::class.java)

        for (handler in contextMenus) {
            try {
                val type = handler.getAnnotation(LoadContextMenu::class.java).type
                if (type == ContextMenuType.USER) {
                    contextMenuListener.addUserContextMenu(handler)
                } else if (type == ContextMenuType.MESSAGE) {
                    contextMenuListener.addMessageContextMenu(handler)
                } else {
                    println("$LogError Context Menu Type not found")
                }
            } catch (exception: InstantiationException) {
                exception.printStackTrace()
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            }
        }
    }

    fun registerListeners(path: String) {
        val listeners = Reflections(path).getTypesAnnotatedWith(LoadListener::class.java)
        for(clazz in listeners) {
            discordApi.addListener(clazz.getDeclaredConstructor().newInstance() as GloballyAttachableListener)
            println("${LogSuccess} Registered Listener \"${clazz.name.split(".").last()}\"")
        }

        println("$LogSuccess Successfully loaded ${listeners.size} Listeners")
    }

    companion object {
        lateinit var instance: Client
    }
}