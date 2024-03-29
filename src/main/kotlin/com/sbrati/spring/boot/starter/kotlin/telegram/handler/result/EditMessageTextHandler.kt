package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.EditMessageText
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.ReplyViewResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class EditMessageTextHandler(private val telegramMessageResolver: TelegramMessageResolver) :
    ResultHandler<EditMessageText>(EditMessageText::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var replyViewResolver: ReplyViewResolver

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: EditMessageText) {
        val replyMarkup = replyViewResolver.resolve(chatId, resultPayload.replyView)
        val text = telegramMessageResolver.resolve(
            chatId = chatId,
            key = resultPayload.key,
            args = resultPayload.args
        )
        bot.editMessageText(
            chatId = ChatId.fromId(chatId),
            messageId = resultPayload.messageId,
            text = text,
            parseMode = resultPayload.parseMode,
            replyMarkup = replyMarkup
        )
    }
}