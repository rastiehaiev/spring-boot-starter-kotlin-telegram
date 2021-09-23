package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import com.github.kotlintelegrambot.entities.ReplyMarkup

class BotVoice(
    var chatId: Long,
    var audioId: String,
    var replyMarkup: ReplyMarkup? = null,
    var disableNotification: Boolean = false,
) : BotResultEntity {
}