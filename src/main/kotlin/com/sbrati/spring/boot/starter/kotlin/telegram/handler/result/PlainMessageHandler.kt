package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.PlainMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import org.springframework.stereotype.Component

@Component
class PlainMessageHandler : AbstractMessageHandler<PlainMessage>(PlainMessage::class.java) {

    override fun messages(chatId: Long, message: PlainMessage): List<BotResultEntity> {
        val text = message.text
        return text?.let {
            val replyMarkup = replyMarkup(chatId, message.replyView)
            listOf<BotResultEntity>(BotMessage(chatId = chatId, text = text, parseMode = message.parseMode, replyMarkup = replyMarkup))
        }.orEmpty()
    }
}