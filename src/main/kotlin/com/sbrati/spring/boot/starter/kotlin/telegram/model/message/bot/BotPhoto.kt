package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import me.ivmg.telegram.entities.ReplyMarkup

class BotPhoto(var chatId: Long,
               var photo: String,
               var caption: String? = null,
               var replyMarkup: ReplyMarkup? = null) : BotResultEntity {
}