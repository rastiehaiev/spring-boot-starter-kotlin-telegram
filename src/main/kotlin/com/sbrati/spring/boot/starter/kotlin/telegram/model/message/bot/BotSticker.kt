package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import com.github.kotlintelegrambot.entities.ReplyMarkup

class BotSticker(
    var chatId: Long,
    var sticker: String,
    var disableNotification: Boolean? = null,
    var replyToMessageId: Long? = null,
    var replyMarkup: ReplyMarkup? = null
) : BotResultEntity