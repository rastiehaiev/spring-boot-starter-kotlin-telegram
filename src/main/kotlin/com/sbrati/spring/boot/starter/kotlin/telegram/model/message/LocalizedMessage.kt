package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.github.kotlintelegrambot.entities.ParseMode
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView

open class LocalizedMessage(
    var key: String = "",
    var args: List<Any> = emptyList(),
    var parseMode: ParseMode? = null,
    var replyView: ReplyView? = null
) : MessageSpec

fun message(key: String, args: List<Any> = emptyList()): LocalizedMessage {
    val message = LocalizedMessage()
    message.key = key
    message.args = args
    return message
}

fun message(builder: LocalizedMessage.() -> Unit): LocalizedMessage {
    return LocalizedMessage().apply(builder)
}