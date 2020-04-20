package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView
import me.ivmg.telegram.entities.ParseMode

class PlainMessage(var text: String? = null,
                   var parseMode: ParseMode? = null,
                   var replyView: ReplyView? = null) : MessageSpec

fun plainMessage(text: String, parseMode: ParseMode? = null, replyView: ReplyView? = null): PlainMessage {
    return PlainMessage(text, parseMode, replyView)
}

fun plainMessage(builder: PlainMessage.() -> Unit): PlainMessage {
    return PlainMessage().apply(builder)
}