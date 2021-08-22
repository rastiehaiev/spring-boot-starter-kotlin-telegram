package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.github.kotlintelegrambot.entities.ParseMode
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView
import java.io.File

open class PhotoMessage(
    var captionKey: String = "",
    var captionArgs: List<Any> = emptyList(),
    var parseMode: ParseMode? = null,
    var replyView: ReplyView? = null,
    var file: File? = null,
) : MessageSpec

fun photoMessage(builder: PhotoMessage.() -> Unit): PhotoMessage {
    return PhotoMessage().apply(builder)
}