package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.sbrati.spring.boot.starter.kotlin.telegram.model.ChatActionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ChatActionResponseHandler : ResultHandler<ChatActionResponse>(ChatActionResponse::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: ChatActionResponse) {
        bot.sendChatAction(chatId = ChatId.fromId(chatId), action = resultPayload.chatAction)
    }
}