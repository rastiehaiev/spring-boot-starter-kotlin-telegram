package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import me.ivmg.telegram.entities.ParseMode
import me.ivmg.telegram.entities.ReplyMarkup

class BotMessage(var chatId: Long,
                 var text: String,
                 var parseMode: ParseMode? = null,
                 var disableWebPagePreview: Boolean? = null,
                 var disableNotification: Boolean? = null,
                 var replyToMessageId: Long? = null,
                 var replyMarkup: ReplyMarkup? = null) : BotResultEntity