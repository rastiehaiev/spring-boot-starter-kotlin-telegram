package com.sbrati.spring.boot.starter.kotlin.telegram.model.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView

open class StickerMessage(
    var sticker: String = "",
    var replyView: ReplyView? = null,
    var replyToMessageId: Long? = null,
    var disableNotification: Boolean = false,
) : MessageSpec

fun sticker(builder: StickerMessage.() -> Unit): StickerMessage {
    return StickerMessage().apply(builder)
}