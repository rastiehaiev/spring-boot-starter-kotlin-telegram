package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyMarkup

class BotMessage(
    var chatId: Long,
    var text: String,
    var parseMode: ParseMode? = null,
    var disableWebPagePreview: Boolean? = null,
    var disableNotification: Boolean? = null,
    var replyToMessageId: Long? = null,
    var replyMarkup: ReplyMarkup? = null
) : BotResultEntity