package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.sbrati.spring.boot.starter.kotlin.telegram.model.EditReplyMarkup
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.ReplyViewResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class EditReplyMarkupHandler : ResultHandler<EditReplyMarkup>(EditReplyMarkup::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var replyViewResolver: ReplyViewResolver

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: EditReplyMarkup) {
        bot.editMessageReplyMarkup(chatId = ChatId.fromId(chatId),
            messageId = resultPayload.messageId,
            replyMarkup = replyViewResolver.resolve(chatId, resultPayload.replyView))
    }
}