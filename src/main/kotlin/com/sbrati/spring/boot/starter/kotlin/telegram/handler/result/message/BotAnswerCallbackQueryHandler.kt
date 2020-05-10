package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResult
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotAnswerCallbackQuery
import org.springframework.stereotype.Component

@Component
class BotAnswerCallbackQueryHandler : BotHandler<BotAnswerCallbackQuery>(BotAnswerCallbackQuery::class.java) {

    override fun handle(botMessage: BotAnswerCallbackQuery): BotResult {
        bot.answerCallbackQuery(
                callbackQueryId = botMessage.callbackQueryId,
                url = botMessage.url,
                text = botMessage.text,
                showAlert = botMessage.showAlert,
                cacheTime = botMessage.cacheTime)
        return BotResult(BotResultStatus.OK, null)
    }
}