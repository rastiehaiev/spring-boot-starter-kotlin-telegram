package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ReplyKeyboardRemove
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.replyview.AbstractReplyViewResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.model.EditReplyMarkup
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.NoReplyView
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class EditReplyMarkupHandler : ResultHandler<EditReplyMarkup>(EditReplyMarkup::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private var replyViewResolvers: List<AbstractReplyViewResolver<*>> = emptyList()

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: EditReplyMarkup) {
        bot.editMessageReplyMarkup(chatId = ChatId.fromId(chatId),
            messageId = resultPayload.messageId,
            replyMarkup = replyMarkup(chatId, resultPayload.replyView))
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