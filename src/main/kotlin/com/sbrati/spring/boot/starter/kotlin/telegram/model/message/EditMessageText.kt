package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.github.kotlintelegrambot.entities.ParseMode
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView


class EditMessageText(
    var messageId: Long = 0,
    var key: String = "",
    var args: List<Any> = emptyList(),
    var parseMode: ParseMode? = null,
    var replyView: ReplyView? = null
)

fun editMessageText(builder: EditMessageText.() -> Unit): EditMessageText {
    return EditMessageText().apply(builder)
}