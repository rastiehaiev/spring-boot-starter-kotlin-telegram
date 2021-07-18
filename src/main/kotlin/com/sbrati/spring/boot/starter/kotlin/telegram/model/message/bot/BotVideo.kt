package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import com.github.kotlintelegrambot.entities.ReplyMarkup

class BotVideo(var chatId: Long,
               var fileId: String,
               var caption: String? = null,
               var replyMarkup: ReplyMarkup? = null) : BotResultEntity {
}