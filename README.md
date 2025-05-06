# 🚀 Javacord Boilerplate

A simple boilerplate for Discord bots using Javacord and Kotlin. This project provides a basic structure for developing Discord bots with slash commands, context menus, and event listeners.

## ✨ Key Features

- 🔄 Automatic Registration for
  - ⚡ Slash commands
  - 🎯 Message & user Context menus
  - 👂 Event listeners of any kind
- ⚙️ Simple configuration via .env file

## 📋 Prerequisites

- ☕ Java 17 or higher
- 🛠️ Gradle
- 🤖 Discord Bot Application

## 🔧 Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/JavacordBoilerplate.git
```

2. Copy `.env.example` to `.env`:
```bash
cp .env.example .env
```

3. Add your Discord Bot Token to `.env`:
```
DISCORD_TOKEN=your_discord_bot_token
SERVER_OVERWRITE=your_server_id  # Optional: Register commands and contexts only in this server
```

4. Build the project:
```bash
./gradlew build
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
    type = ContextMenuType.MESSAGE,            // Or ContextMenuType.USER
    permission = PermissionType.MANAGE_MESSAGES, // Optional: Required permission
    enabledInDms = true                        // Optional: Whether the menu is available in DMs
)
class Greet: IMessageContextMenu {
    override fun perform(event: MessageContextMenuCommandEvent): Unit = with(event.messageContextMenuInteraction) {
        event.api.getUserById(user.id).thenAccept { user ->
            createImmediateResponder()
                .setContent("Howdy ${user.name}!")
                .respond()
        }
    }
}
```

### 👂 Creating Event Listeners

Create a new class in `src/main/kotlin/tech/mdxwzl/listeners/`:

```kotlin
@LoadListener
class ReactionListener: ReactionAddListener {  // or any other listener type
    override fun onReactionAdd(event: ReactionAddEvent) {
        // Your logic here
    }
}
```

## 📁 Project Structure

```
src/main/kotlin/tech/mdxwzl/
├── annotations/                 # Annotations for commands and events
│   ├── LoadSlashCommand.kt
│   ├── LoadContextMenu.kt
│   └── LoadListener.kt
├── commands/                    # Slash commands
├── contexts/                    # Context menus
├── enums/                       # Enums and constants
│   └── ContextMenuType.kt
├── interfaces/                  # Interface definitions
│   ├── ISlashCommand.kt
│   ├── IMessageContextMenu.kt
│   └── IUserContextMenu.kt
├── listeners/                   # Event listeners
│   ├── SlashCommandListener.kt  
│   ├── ContextMenuListener.kt   
├── utils/                       # Utility functions
├── Client.kt                    # Main Discord client class
└── Main.kt                      # Application entry point
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an Issue for suggestions.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 