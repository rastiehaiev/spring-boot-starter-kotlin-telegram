package com.sbrati.spring.boot.starter.kotlin.telegram.model

import me.ivmg.telegram.entities.ParseMode

class Message {
    var key: String = ""
    var args: List<String> = emptyList()
    var plainText: String? = null
    var parseMode: ParseMode? = null
    var inlineKeyboard: InlineKeyboard? = null
    var keyboard: Keyboard? = null
}

fun message(key: String, args: List<String> = emptyList()): Message {
    val message = Message()
    message.key = key
    message.args = args
    return message
}

fun noMessage(): Message {
    return Message()
}

fun message(builder: Message.() -> Unit): Message {
    return Message().apply(builder)
}