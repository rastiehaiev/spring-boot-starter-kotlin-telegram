package com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler

import com.sbrati.spring.boot.starter.kotlin.telegram.bot.TelegramBot
import com.sbrati.spring.boot.starter.kotlin.telegram.model.AnswerCallbackQuery
import org.springframework.stereotype.Component

@Component
class AnswerCallbackQueryResultHandler(private val telegramBot: TelegramBot) : ResultHandler<AnswerCallbackQuery>(AnswerCallbackQuery::class.java) {

    override fun handle(chatId: Long, resultPayload: AnswerCallbackQuery) {
        telegramBot.answerCallbackQuery(chatId, resultPayload)
    }
}