package com.sbrati.spring.boot.starter.kotlin.telegram.model

import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView

class EditReplyMarkup(val messageId: Long, val replyView: ReplyView)

fun editReplyMarkup(messageId: Long, replyView: ReplyView): EditReplyMarkup {
    return EditReplyMarkup(messageId, replyView)
}