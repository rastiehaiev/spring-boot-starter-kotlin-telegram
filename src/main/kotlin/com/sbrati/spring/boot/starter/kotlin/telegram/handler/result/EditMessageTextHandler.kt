package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ReplyKeyboardRemove
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.replyview.AbstractReplyViewResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.EditMessageText
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.NoReplyView
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class EditMessageTextHandler(private val telegramMessageResolver: TelegramMessageResolver) :
    ResultHandler<EditMessageText>(EditMessageText::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private var replyViewResolvers: List<AbstractReplyViewResolver<*>> = emptyList()

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, message: EditMessageText) {
        val replyMarkup = replyMarkup(chatId, message.replyView)
        val text = telegramMessageResolver.resolve(
            chatId = chatId,
            key = message.key,
            args = message.args
        )
        bot.editMessageText(
            chatId = ChatId.fromId(chatId),
            messageId = message.messageId,
            text = text,
            parseMode = message.parseMode,
            replyMarkup = replyMarkup
        )
    }

    private fun replyMarkup(chatId: Long, replyView: ReplyView?): ReplyMarkup? {
        if (replyView is NoReplyView) {
            return null
        }
        return replyView?.let {
            val resolver: AbstractReplyViewResolver<Any>? =
                replyViewResolvers.firstOrNull { it.isApplicable(replyView) } as AbstractReplyViewResolver<Any>?
            resolver?.handle(chatId, replyView)
        }.orElse { ReplyKeyboardRemove() }
    }
}