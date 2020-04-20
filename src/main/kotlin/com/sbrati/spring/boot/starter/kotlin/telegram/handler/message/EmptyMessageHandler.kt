package com.sbrati.spring.boot.starter.kotlin.telegram.handler.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.EmptyMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import org.springframework.stereotype.Component

@Component
class EmptyMessageHandler : AbstractMessageHandler<EmptyMessage>(EmptyMessage::class.java) {

    override fun messages(chatId: Long, message: EmptyMessage): List<BotResultEntity> {
        return emptyList()
    }
}