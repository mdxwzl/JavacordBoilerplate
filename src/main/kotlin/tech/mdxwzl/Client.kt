package tech.mdxwzl

import io.github.cdimascio.dotenv.dotenv
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.interaction.ApplicationCommandBuilder
import org.javacord.api.listener.GloballyAttachableListener
import tech.mdxwzl.listeners.SlashCommandListener
import tech.mdxwzl.utils.LogSuccess
import org.reflections8.Reflections
import tech.mdxwzl.annotations.LoadListener
import tech.mdxwzl.annotations.LoadSlashCommand
import tech.mdxwzl.utils.LogError

class Client(token: String) {
    lateinit var discordApi: DiscordApi
    lateinit var slashCommandListener: SlashCommandListener

    val commands = mutableSetOf<ApplicationCommandBuilder<*, *, *>>()

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
    }

    fun registerSlashCommands(path: String) {
        val reflections = Reflections(path)

        for (handler in reflections.getTypesAnnotatedWith(LoadSlashCommand::class.java)) {
            try {
                commands.add(slashCommandListener.addSlashCommand(handler))
            } catch (exception: InstantiationException) {
                exception.printStackTrace()
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            }
        }

        val serverOverwrite = dotenv().get("SERVER_OVERWRITE")

        if (serverOverwrite == "") {
            overwriteGlobalCommands()
            return;
        }

        overwriteServerCommands(serverOverwrite.toLong())
    }

    private fun overwriteGlobalCommands() {
        discordApi.bulkOverwriteGlobalApplicationCommands(commands).thenAccept { command ->
            println("$LogSuccess Overwrote ${command.size} global commands")
        }.exceptionally { exception ->
            println("Failed to overwrite global commands: ${exception.message}")
            null
        }
    }

    private fun overwriteServerCommands(serverId: Long) {
        val server = discordApi.getServerById(serverId).orElse(null)

        if (server == null) {
            println("$LogError Server($serverId) not found")
            return
        }

        discordApi.bulkOverwriteServerApplicationCommands(server, commands).thenAccept { command ->
            println("$LogSuccess Overwrote server commands for \"${server.name}\" with ${command.size} commands")
        }.exceptionally { exception ->
            println("Failed to overwrite server commands: ${exception.message}")
            null
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