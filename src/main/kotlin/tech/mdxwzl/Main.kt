package tech.mdxwzl

fun main() {
    val client = Client()
    client.registerSlashCommands("tech.mdxwzl.commands")
    client.registerListeners("tech.mdxwzl.listeners")
    client.registerContextMenus("tech.mdxwzl.contexts")

    return;
}
