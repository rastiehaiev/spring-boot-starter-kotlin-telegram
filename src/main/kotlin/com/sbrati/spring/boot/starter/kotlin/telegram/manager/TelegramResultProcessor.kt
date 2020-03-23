package com.sbrati.spring.boot.starter.kotlin.telegram.manager

import com.sbrati.spring.boot.starter.kotlin.telegram.bot.TelegramBot
import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.model.AnswerCallbackQuery
import com.sbrati.spring.boot.starter.kotlin.telegram.model.FinishWithMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.Message
import com.sbrati.spring.boot.starter.kotlin.telegram.model.NoHandlerFound
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import me.ivmg.telegram.Bot
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class TelegramResultProcessor(private val applicationContext: ApplicationContext,
                              private val telegramMessageResolver: TelegramMessageResolver,
                              private val contextRepository: TelegramCommandContextRepository) {

    private val logger by LoggerDelegate()

    // this is done to get rid of cyclic dependency
    private val bot: TelegramBot by lazy {
        TelegramBot(applicationContext.getBean(Bot::class.java), telegramMessageResolver, contextRepository)
    }

    fun processResult(chatId: Long, result: Any?) {
        result?.let {
            when (result) {
                is Message -> {
                    bot.sendMessage(chatId, result)
                }
                is AnswerCallbackQuery -> {
                    bot.answerCallbackQuery(chatId, result)
                }
                is FinishWithMessage -> {
                    bot.sendMessage(chatId, result.message)
                    removeContextForUser(chatId)
                }
                is NoHandlerFound -> logger.debug("No handler found.")
                is Unit -> logger.debug("Nothing to process.")
                else -> logger.debug("Result: {}", result)
            }
        }
    }

    private fun removeContextForUser(chatId: Long) {
        logger.debug("Removing context for user with chatID={}.", chatId)
        contextRepository.removeByChatId(chatId)
    }
}