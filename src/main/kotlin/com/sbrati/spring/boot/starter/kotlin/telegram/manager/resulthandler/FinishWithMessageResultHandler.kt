package com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler

import com.sbrati.spring.boot.starter.kotlin.telegram.bot.TelegramBot
import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.model.FinishWithMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.stereotype.Component

@Component
class FinishWithMessageResultHandler(private val telegramBot: TelegramBot,
                                     private val contextRepository: TelegramCommandContextRepository) : ResultHandler<FinishWithMessage>(FinishWithMessage::class.java) {

    private val logger by LoggerDelegate()

    override fun handle(chatId: Long, resultPayload: FinishWithMessage) {
        telegramBot.sendMessage(chatId, resultPayload.message)
        logger.debug("Removing context for user with chatID={}.", chatId)
        contextRepository.removeByChatId(chatId)
    }
}