package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.Update

class ForwardedMessage(var original: Update? = null,
                       var textWrapperKey: String? = null,
                       var args: List<String>? = null,
                       var parseMode: ParseMode? = null,
                       var replyView: ReplyView? = null) : MessageSpec

fun forwardedMessage(builder: ForwardedMessage.() -> Unit): ForwardedMessage {
    return ForwardedMessage().apply(builder)
}

fun forwardedMessage(update: Update,
                     textWrapperKey: String? = null,
                     args: List<String>? = null,
                     parseMode: ParseMode? = null): ForwardedMessage {
    return ForwardedMessage(update, textWrapperKey, args, parseMode)
}