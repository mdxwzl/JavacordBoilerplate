# 🚀 Javacord Boilerplate

A simple boilerplate for Discord bots using Javacord and Kotlin. This project provides a basic structure for developing Discord bots with slash commands, context menus, and event listeners.

## ✨ Key Features

- 🔄 Automatic Registration for
  - ⚡ Slash commands
  - 🎯 Message & user Context menus
  - 👂 Event listeners of any kind
- 📜 Logging functions to easily track events
- ⚙️ Simple configuration via .env file

## 📋 Prerequisites

- ☕ Java 17 or higher
- 🤖 Discord Bot Application

## 🔧 Installation

1. Clone the repository:
```bash
git clone https://github.com/mdxwzl/JavacordBoilerplate.git
```

2. Build the project (will generate a jar in build/libs/):
```cmd
cd JavacordBoilerplate
gradlew.bat shadowJar
```

3. Copy `.env.example` to `build/libs/.env`:
```cmd
cp .env.example build/libs/.env
```

4. Add your Discord Bot Token to `.env`:
```
DISCORD_TOKEN=your_discord_bot_token
SERVER_OVERWRITE=your_server_id  # Optional: Register commands and contexts only in this server
```

5. Run the bot:
```cmd
cd build/libs
java -jar JavacordBoilerplate...jar
```

## 📝 Usage

### 🤖 Creating a Client

The Client class handles initialization and registration of all components:

```kotlin
fun main() {
    val client = Client()
    client.registerSlashCommands("tech.mdxwzl.commands")
    client.registerListeners("tech.mdxwzl.listeners")
    client.registerContextMenus("tech.mdxwzl.contexts")
}
```

### ⚡ Creating Slash Commands

Create a new class in `src/main/kotlin/tech/mdxwzl/commands/`:

Simple Command without Options:
```kotlin
@LoadSlashCommand(
    name = "ping",
    description = "Check if the bot is alive"
)
class Ping: ISlashCommand {
    override fun perform(event: SlashCommandCreateEvent): Unit = with(event.slashCommandInteraction) { 
        log("Ping command executed by ${user.id}")
        createImmediateResponder()
            .setContent("Pong!")
            .respond()
    }
}
```

Command with Options:
```kotlin
@LoadSlashCommand(
    name = "repeat",
    description = "Repeat the input string",
    options = true
)
class Repeat: HasOptions {
    override fun perform(event: SlashCommandCreateEvent): Unit = with(event.slashCommandInteraction) {
      log("Repeat command executed by ${user.id}")
      createImmediateResponder()
        .setContent(options[0].stringValue.get())
        .respond()
    }

    override fun getOptions(): List<SlashCommandOption> {
        return listOf(
            SlashCommandOption.createStringOption("input", "This is a string option", true)
        )
    }
}
```

### 🎯 Creating Context Menus

Create a new class in `src/main/kotlin/tech/mdxwzl/contexts/`:

Message Context Menu:
```kotlin
@LoadContextMenu(
    name = "greet",
    type = ContextMenuType.USER,            // Or ContextMenuType.MESSAGE
    permission = PermissionType.MANAGE_MESSAGES, // Optional: Required permission
    enabledInDms = true                        // Optional: Whether the menu is available in DMs
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
```

### 👂 Creating Event Listeners

Create a new class in `src/main/kotlin/tech/mdxwzl/listeners/`:

```kotlin
...
import tech.mdxwzl.utils.log

@LoadListener
class ReactionListener: ReactionAddListener { // or any other listener type
  override fun onReactionAdd(event: ReactionAddEvent) {
    // Your logic here
    log("Reaction added by ${event.userId}")
  }
}
```

## 📁 Project Structure

```
src/main/kotlin/tech/mdxwzl/
├── annotations/                 # Annotations for commands and events
│   ├── LoadContextMenu.kt
│   └── LoadListener.kt
│   ├── LoadSlashCommand.kt
├── commands/                    # Slash commands
├── contexts/                    # Context menus
├── enums/                       # Enums and constants
│   └── ContextMenuType.kt
├── interfaces/                  # Interface definitions
│   ├── HasOptions.kt
│   ├── IContextMenu.kt
│   ├── IMessageContextMenu.kt
│   ├── ISlashCommand.kt
│   └── IUserContextMenu.kt
├── listeners/                   # Event listeners
│   ├── SlashCommandListener.kt  
│   ├── ContextMenuListener.kt   
├── utils/                       # Utility functions
│   ├── Colors.kit
│   ├── Logs.kit
├── Client.kt                    # Main Discord client class
└── Main.kt                      # Application entry point
```

## ⌨️ Logging
Each slash command, context menu and event listener has it's own logging function.
It will include crucial details from the origin of the call.
For listeners, it has to be imported from the `tech.mdxwzl.utils` package.

The output will look like this:
```
[LOG] [COMMAND - ping] Ping command executed by 337576581406916631
[LOG] [COMMAND - repeat] Repeat command executed by 337576581406916631
[LOG] [CONTEXT - greet] Greet command executed by 337576581406916631 for 1313706513923182703
[LOG] [EVENT - ReactionListener] Reaction added by 337576581406916631
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an Issue for suggestions.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 