package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyMarkup

class BotVideo(
    var chatId: Long,
    var caption: String? = null,
    var parseMode: ParseMode? = null,
    var replyMarkup: ReplyMarkup? = null,
    var fileId: String = ""
) : BotResultEntity
