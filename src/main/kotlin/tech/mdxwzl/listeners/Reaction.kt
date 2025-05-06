package tech.mdxwzl.listeners

import org.javacord.api.event.message.reaction.ReactionAddEvent
import org.javacord.api.listener.message.reaction.ReactionAddListener
import tech.mdxwzl.annotations.LoadListener

@LoadListener
class Reaction: ReactionAddListener {
    override fun onReactionAdd(event: ReactionAddEvent) {
        if (event.userId == event.api.yourself.id) return

        event.requestMessage().join().addReaction("💀").join()
        println("Reaction added by ${event.userId}")

    }
}