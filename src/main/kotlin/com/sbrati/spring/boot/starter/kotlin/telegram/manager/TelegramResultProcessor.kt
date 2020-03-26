package com.sbrati.spring.boot.starter.kotlin.telegram.manager

import com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler.ResultHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.NoHandlerFound
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TelegramResultProcessor {

    private val logger by LoggerDelegate()

    @Autowired
    private lateinit var resultHandlers: List<ResultHandler<*>>

    fun processResult(chatId: Long, result: Any?) {
        result?.let {
            when (result) {
                is NoHandlerFound -> logger.debug("No handler found.")
                is Unit -> logger.debug("Nothing to process.")
                else -> resultHandlers.firstOrNull { handler -> handler.isApplicable(result) }?.let {
                    val resultHandler: ResultHandler<Any> = it as ResultHandler<Any>
                    resultHandler.handle(chatId, result)
                }
            }
        }
    }
}