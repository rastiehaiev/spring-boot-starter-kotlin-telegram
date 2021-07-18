package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.entities.ReplyKeyboardRemove
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.sbrati.spring.boot.starter.kotlin.telegram.component.BlockedChatHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.replyview.AbstractReplyViewResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message.BotHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.MessageSpec
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.NoReplyView
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.ReplyView
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

abstract class AbstractMessageHandler<M : MessageSpec>(type: Class<M>) : ResultHandler<M>(type) {

    @Autowired
    private var replyViewResolvers: List<AbstractReplyViewResolver<*>> = emptyList()

    @Autowired
    private var botHandlers: List<BotHandler<*>> = emptyList()

    @Autowired
    private var blockedChatHandler: BlockedChatHandler? = null

    @Autowired
    private lateinit var applicationContext: ApplicationContext


    override fun handle(chatId: Long, resultPayload: M) {
        val messages = messages(chatId, resultPayload)
        for (entity in messages) {
            val handler = botHandlers.firstOrNull { it.isApplicable(entity) } as BotHandler<Any>?
            val botResult = handler?.handle(entity)
            botResult?.let {
                if (botResult.status == BotResultStatus.FORBIDDEN) {
                    val blockedUserChatId = botResult.chatId
                    if (blockedUserChatId != null) {
                        blockedChatHandler?.onChatIdBlocked(blockedUserChatId)
                    }
                }
            }
        }
    }

    protected fun replyMarkup(chatId: Long, replyView: ReplyView?): ReplyMarkup? {
        if (replyView is NoReplyView) {
            return null
        }
        return replyView?.let {
            val resolver: AbstractReplyViewResolver<Any>? = replyViewResolvers.firstOrNull { it.isApplicable(replyView) } as AbstractReplyViewResolver<Any>?
            resolver?.handle(chatId, replyView)
        }.orElse { ReplyKeyboardRemove() }
    }

    protected abstract fun messages(chatId: Long, message: M): List<BotResultEntity>
}