package com.sbrati.spring.boot.starter.kotlin.telegram.model

class CompoundMessage {

    var message: Message? = null
    var adminMessage: Message? = null

    fun admin(message: () -> Message) {
        this.adminMessage = message()
    }

    fun user(message: () -> Message) {
        this.message = message()
    }
}

fun compoundMessage(builder: CompoundMessage.() -> Unit): CompoundMessage = CompoundMessage().apply(builder)

