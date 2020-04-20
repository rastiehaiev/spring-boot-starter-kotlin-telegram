package com.sbrati.spring.boot.starter.kotlin.telegram.handler.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.LocalizedMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.stereotype.Component

@Component
class LocalizedMessageHandler(private val telegramMessageResolver: TelegramMessageResolver)
    : AbstractMessageHandler<LocalizedMessage>(LocalizedMessage::class.java) {

    override fun messages(chatId: Long, message: LocalizedMessage): List<BotResultEntity> {
        val replyMarkup = replyMarkup(chatId, message.replyView)
        val text = telegramMessageResolver.resolve(chatId = chatId,
                key = message.key,
                args = message.args)
        return listOf(BotMessage(chatId = chatId, text = text, parseMode = message.parseMode, replyMarkup = replyMarkup))
    }
}