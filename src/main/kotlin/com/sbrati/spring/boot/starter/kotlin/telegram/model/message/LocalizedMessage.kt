package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView
import me.ivmg.telegram.entities.ParseMode

open class LocalizedMessage(var key: String = "",
                            var args: List<String> = emptyList(),
                            var parseMode: ParseMode? = null,
                            var replyView: ReplyView? = null) : MessageSpec

fun message(key: String, args: List<String> = emptyList()): LocalizedMessage {
    val message = LocalizedMessage()
    message.key = key
    message.args = args
    return message
}

fun message(builder: LocalizedMessage.() -> Unit): LocalizedMessage {
    return LocalizedMessage().apply(builder)
}