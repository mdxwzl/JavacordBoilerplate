package tech.mdxwzl.interfaces

import tech.mdxwzl.annotations.LoadContextMenu
import tech.mdxwzl.utils.BLUE
import tech.mdxwzl.utils.LogInfo
import tech.mdxwzl.utils.WHITE

interface IContextMenu {
    fun log(content: String) {
        val name = this::class.annotations
            .filterIsInstance<LoadContextMenu>()
            .firstOrNull()
            ?.name ?: this::class.simpleName

        println("$LogInfo $BLUE[CONTEXT - $name]$WHITE $content")
    }
}