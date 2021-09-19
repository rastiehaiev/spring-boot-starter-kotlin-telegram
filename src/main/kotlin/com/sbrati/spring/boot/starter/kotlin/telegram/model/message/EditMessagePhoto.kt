package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.github.kotlintelegrambot.entities.ParseMode
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView


class EditMessagePhoto(
    var messageId: Long = 0,
    var captionKey: String = "",
    var captionArgs: List<Any> = emptyList(),
    var photoFileId: String = "",
    var parseMode: ParseMode? = null,
    var replyView: ReplyView? = null
)

fun editMessagePhoto(builder: EditMessagePhoto.() -> Unit): EditMessagePhoto {
    return EditMessagePhoto().apply(builder)
}