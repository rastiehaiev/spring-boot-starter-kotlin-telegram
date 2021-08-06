package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView


class EditMessageVideo(
    var messageId: Long = 0,
    var videoFileId: String = "",
    var replyView: ReplyView? = null
)

fun editMessageVideo(builder: EditMessageVideo.() -> Unit): EditMessageVideo {
    return EditMessageVideo().apply(builder)
}