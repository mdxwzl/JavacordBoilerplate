package tech.mdxwzl.utils

import org.javacord.api.listener.GloballyAttachableListener

fun GloballyAttachableListener.log(content: String) {
    println("$LogInfo $BLUE[EVENT - ${this::class.simpleName}]$WHITE $content")
}