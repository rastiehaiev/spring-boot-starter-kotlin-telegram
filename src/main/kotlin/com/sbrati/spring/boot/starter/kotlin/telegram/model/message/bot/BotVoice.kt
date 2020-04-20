package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import me.ivmg.telegram.entities.ReplyMarkup

class BotVoice(var chatId: Long,
               var audioId: String,
               var replyMarkup: ReplyMarkup? = null) : BotResultEntity {
}