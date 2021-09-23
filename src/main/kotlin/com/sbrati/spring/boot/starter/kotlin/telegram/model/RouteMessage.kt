package com.sbrati.spring.boot.starter.kotlin.telegram.model

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.MessageSpec

class RouteMessage(
    var senderMessage: MessageSpec? = null,
    var everyoneMessage: ((Long) -> MessageSpec)? = null,
    var adminsMessage: MessageSpec? = null,
    var receiverMessage: MessageSpec? = null,
    var receiverChatId: Long? = null,
) {

    fun sender(spec: () -> MessageSpec) {
        this.senderMessage = spec()
    }

    fun everyone(spec: (Long) -> MessageSpec) {
        this.everyoneMessage = spec
    }

    fun admin(spec: () -> MessageSpec) {
        this.adminsMessage = spec()
    }

    fun receiver(chatId: Long, spec: () -> MessageSpec) {
        this.receiverMessage = spec()
        this.receiverChatId = chatId
    }
}

fun route(builder: RouteMessage.() -> Unit): RouteMessage {
    return RouteMessage().apply(builder)
}



