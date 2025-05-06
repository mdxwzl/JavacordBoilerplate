package tech.mdxwzl

import io.github.cdimascio.dotenv.dotenv

fun main() {
    val client = Client(dotenv().get("DISCORD_TOKEN") ?: error("DISCORD_TOKEN not found in .env"))
    client.registerSlashCommands("tech.mdxwzl.commands")
    client.registerListeners("tech.mdxwzl.listeners")

    return;
}
