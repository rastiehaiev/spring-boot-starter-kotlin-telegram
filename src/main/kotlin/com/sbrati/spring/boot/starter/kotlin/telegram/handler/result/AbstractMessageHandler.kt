package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.sbrati.spring.boot.starter.kotlin.telegram.component.BlockedChatHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message.BotHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.MessageSpec
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.ReplyViewResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

abstract class AbstractMessageHandler<M : MessageSpec>(type: Class<M>) : ResultHandler<M>(type) {

    @Autowired
    protected lateinit var replyViewResolver: ReplyViewResolver

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

    protected abstract fun messages(chatId: Long, message: M): List<BotResultEntity>
}