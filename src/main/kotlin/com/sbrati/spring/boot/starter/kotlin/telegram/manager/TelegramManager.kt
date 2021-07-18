package com.sbrati.spring.boot.starter.kotlin.telegram.manager

import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.sbrati.spring.boot.starter.kotlin.telegram.util.chatId
import com.sbrati.telegram.domain.Event
import com.github.kotlintelegrambot.entities.Update
import org.springframework.stereotype.Component

@Component
open class TelegramManager(
    private val operationsManager: TelegramOperationsManager,
    private val resultProcessor: TelegramResultProcessor
) {

    private val log by LoggerDelegate()

    fun onUpdate(update: Update) {
        try {
            val result = operationsManager.onUpdate(update)
            val chatId = update.chatId() ?: return
            resultProcessor.processResult(chatId, result)
        } catch (e: Exception) {
            log.error("Failed to process update: $update", e)
        }
    }

    fun onEvent(event: Event<*>) {
        try {
            val result = operationsManager.onEvent(event)
            resultProcessor.processResult(event.chatId, result)
        } catch (e: Exception) {
            log.error("Failed to process event: $event", e)
        }
    }
}