package com.sbrati.spring.boot.starter.kotlin.telegram.manager

import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandExecutionContextProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler.ResultHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.FinishWith
import com.sbrati.spring.boot.starter.kotlin.telegram.model.MultipleResults
import com.sbrati.spring.boot.starter.kotlin.telegram.model.NoHandlerFound
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TelegramResultProcessor(private val executionContextProvider: TelegramCommandExecutionContextProvider) {

    private val logger by LoggerDelegate()

    @Autowired
    private lateinit var resultHandlers: List<ResultHandler<*>>

    fun processResult(chatId: Long, result: Any?) {
        result?.let {
            when (result) {
                is NoHandlerFound -> logger.debug("No handler found.")
                is Unit -> logger.debug("Nothing to process.")
                is MultipleResults -> {
                    result.list.forEach { result ->
                        processResult(chatId, result)
                    }
                }
                is FinishWith -> {
                    handle(chatId, result.result)
                    logger.debug("Removing context for user with chatID={}.", chatId)
                    executionContextProvider.removeByChatId(chatId)
                }
                else -> handle(chatId, result)
            }
        }
    }

    private fun handle(chatId: Long, result: Any) {
        resultHandlers.firstOrNull { handler -> handler.isApplicable(result) }?.let {
            val resultHandler: ResultHandler<Any> = it as ResultHandler<Any>
            resultHandler.handle(chatId, result)
        }
    }
}