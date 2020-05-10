package com.sbrati.spring.boot.starter.kotlin.telegram.handler

import com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.AbstractMessageHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.MessageSpec
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.stereotype.Component

@Component
class MessageHandlers(private val messageHandlers: List<AbstractMessageHandler<*>>) {

    private val logger by LoggerDelegate()

    fun processMessage(chatId: Long, messageSpec: MessageSpec) {
        val messageHandler: AbstractMessageHandler<MessageSpec>?
                = messageHandlers.firstOrNull { handler -> handler.isApplicable(messageSpec) } as AbstractMessageHandler<MessageSpec>?
        if (messageHandler == null) {
            logger.error("No message handler found for message $messageSpec.")
        } else {
            messageHandler.handle(chatId, messageSpec)
        }
    }
}