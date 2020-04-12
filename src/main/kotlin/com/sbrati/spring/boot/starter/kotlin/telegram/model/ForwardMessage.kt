package com.sbrati.spring.boot.starter.kotlin.telegram.model

class ForwardMessage {

    var sender: Message? = null
    var everyone: Message? = null

    fun sender(message: () -> Message) {
        this.sender = message()
    }

    fun everyone(message: () -> Message) {
        this.everyone = message()
    }
}

fun forwardUpdate(builder: ForwardMessage.() -> Unit): ForwardMessage = ForwardMessage().apply(builder)