package com.sbrati.spring.boot.starter.kotlin.telegram.handler.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.AnswerCallbackQuery
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotAnswerCallbackQuery
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.stereotype.Component

@Component
class AnswerCallbackQueryHandler(private val telegramMessageResolver: TelegramMessageResolver)
    : AbstractMessageHandler<AnswerCallbackQuery>(AnswerCallbackQuery::class.java) {

    override fun messages(chatId: Long, message: AnswerCallbackQuery): List<BotResultEntity> {
        if (message.key.isNotBlank()) {
            val text = telegramMessageResolver.resolve(chatId = chatId, key = message.key, args = message.args)
            val result = BotAnswerCallbackQuery(text = text,
                    cacheTime = 20,
                    callbackQueryId = message.callbackQueryId,
                    showAlert = message.showAlert)
            return listOf(result)
        }
        return emptyList()
    }
}