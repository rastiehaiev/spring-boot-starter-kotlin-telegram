package com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler

import com.sbrati.spring.boot.starter.kotlin.telegram.bot.TelegramBot
import com.sbrati.spring.boot.starter.kotlin.telegram.model.Message
import org.springframework.stereotype.Component

@Component
class MessageResultHandler(private val telegramBot: TelegramBot) : ResultHandler<Message>(Message::class.java) {

    override fun handle(chatId: Long, resultPayload: Message) = telegramBot.sendMessage(chatId, resultPayload)
}