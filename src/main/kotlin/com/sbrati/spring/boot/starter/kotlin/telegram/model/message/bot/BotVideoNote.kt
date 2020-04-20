package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import me.ivmg.telegram.entities.ReplyMarkup

class BotVideoNote(val chatId: Long,
                   val videoNoteId: String,
                   val duration: Int? = null,
                   val length: Int? = null,
                   val disableNotification: Boolean? = null,
                   val replyToMessageId: Long? = null,
                   val replyMarkup: ReplyMarkup? = null) : BotResultEntity