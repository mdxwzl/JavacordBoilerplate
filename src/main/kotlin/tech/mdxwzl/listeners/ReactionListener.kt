package tech.mdxwzl.listeners

import org.javacord.api.event.message.reaction.ReactionAddEvent
import org.javacord.api.listener.message.reaction.ReactionAddListener
import tech.mdxwzl.annotations.LoadListener
import tech.mdxwzl.utils.log

@LoadListener
class ReactionListener: ReactionAddListener {
    override fun onReactionAdd(event: ReactionAddEvent) {
        if (event.userId == event.api.yourself.id) return

        event.requestMessage().join().addReaction("💀").join()
        log("Reaction added by ${event.userId}")
    }
}