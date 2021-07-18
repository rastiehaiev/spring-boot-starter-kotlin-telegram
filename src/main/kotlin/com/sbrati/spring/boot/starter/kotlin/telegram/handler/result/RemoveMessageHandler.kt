package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.sbrati.spring.boot.starter.kotlin.telegram.model.RemoveMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class RemoveMessageHandler : ResultHandler<RemoveMessage>(RemoveMessage::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: RemoveMessage) {
        bot.deleteMessage(ChatId.fromId(chatId), resultPayload.messageId)
    }
}