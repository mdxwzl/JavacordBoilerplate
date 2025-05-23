    package tech.mdxwzl.listeners

    import org.javacord.api.event.interaction.SlashCommandCreateEvent
    import org.javacord.api.interaction.SlashCommandBuilder
    import org.javacord.api.listener.interaction.SlashCommandCreateListener
    import tech.mdxwzl.Client
    import tech.mdxwzl.annotations.LoadSlashCommand
    import tech.mdxwzl.interfaces.HasOptions
    import tech.mdxwzl.interfaces.ISlashCommand
    import tech.mdxwzl.utils.LogInfo
    import tech.mdxwzl.utils.LogSuccess

    class SlashCommandListener: SlashCommandCreateListener {
        val discordApi = Client.instance.discordApi
        private val slashCommands = mutableMapOf<String,  ISlashCommand>()

        init {
            discordApi.addSlashCommandCreateListener(this)
            println("$LogInfo Listening for slash commands")
        }

        override fun onSlashCommandCreate(event: SlashCommandCreateEvent): Unit = with(event.slashCommandInteraction) {
            val slashCommand = slashCommands[commandName] ?: return

            // just for safety, discord should handle this on its own
            val permission = slashCommand::class.java.getAnnotation(LoadSlashCommand::class.java).permission
            val server = event.api.getServerById(server.get().id).get()
            if (!server.hasPermission(user, permission)) {
                return
            }

            slashCommand.perform(event)
        }

        fun addSlashCommand(handler: Class<*>) {
            val annotation = handler.getAnnotation(LoadSlashCommand::class.java)
            val name = annotation.name
            val description = annotation.description
            val options = annotation.options
            val permission = annotation.permission
            val enabledInDms = annotation.enabledInDms

            val builder = SlashCommandBuilder()
                .setName(name)
                .setDescription(description)
                .setDefaultEnabledForPermissions(permission)
                .setEnabledInDms(enabledInDms)

            val slashCommand = handler.getConstructor().newInstance() as ISlashCommand
            slashCommands[name] = slashCommand

            if (options) {
                val slashOptions = (slashCommand as HasOptions).getOptions()
                for (option in slashOptions)  {
                    builder.addOption(option)
                }
            }

            if (Client.instance.server == null) {
                builder.createGlobal(discordApi).join()
                println("$LogInfo Registered slash command \"$name\" (global)")
            }else {
                builder.createForServer(Client.instance.server).join()
                println("$LogInfo Registered slash command \"$name\" (${Client.instance.server.name})")
            }
        }
    }