package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.github.kotlintelegrambot.entities.ParseMode
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView

open class VideoMessage(
    var captionKey: String = "",
    var captionArgs: List<Any> = emptyList(),
    var parseMode: ParseMode? = null,
    var replyView: ReplyView? = null,
    var fileId: String = ""
) : MessageSpec

fun videoMessage(builder: VideoMessage.() -> Unit): VideoMessage {
    return VideoMessage().apply(builder)
}