package com.sbrati.spring.boot.starter.kotlin.telegram.manager

import com.sbrati.spring.boot.starter.kotlin.telegram.util.chatId
import com.sbrati.telegram.domain.Event
import me.ivmg.telegram.entities.Update
import org.springframework.stereotype.Component

@Component
open class TelegramManager(private val operationsManager: TelegramOperationsManager,
                           private val resultProcessor: TelegramResultProcessor) {

    fun onUpdate(update: Update) {
        val result = operationsManager.onUpdate(update)
        val chatId = update.chatId() ?: return
        resultProcessor.processResult(chatId, result)
    }

    fun onEvent(event: Event<*>) {
        val result = operationsManager.onEvent(event)
        resultProcessor.processResult(event.chatId, result)
    }
}