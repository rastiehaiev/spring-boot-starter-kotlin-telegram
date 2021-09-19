package com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot

import com.github.kotlintelegrambot.entities.ReplyMarkup
import java.io.File

class BotPhoto(
    var chatId: Long,
    var photo: File? = null,
    var fileId: String = "",
    var caption: String? = null,
    var replyMarkup: ReplyMarkup? = null,
) : BotResultEntity
