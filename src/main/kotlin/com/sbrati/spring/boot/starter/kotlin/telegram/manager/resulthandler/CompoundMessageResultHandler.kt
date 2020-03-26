package com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler

import com.sbrati.spring.boot.starter.kotlin.telegram.bot.TelegramBot
import com.sbrati.spring.boot.starter.kotlin.telegram.component.AdminChatIdsProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.model.CompoundMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CompoundMessageResultHandler(private val telegramBot: TelegramBot) : ResultHandler<CompoundMessage>(CompoundMessage::class.java) {

    @Autowired
    private var adminChatIdsProvider: AdminChatIdsProvider? = null

    override fun handle(chatId: Long, resultPayload: CompoundMessage) {
        val message = resultPayload.message
        message?.let {
            telegramBot.sendMessage(chatId, message)
        }
        val adminMessage = resultPayload.adminMessage
        adminMessage?.let {
            adminChatIdsProvider?.adminChatIds()?.forEach { adminChatId ->
                telegramBot.sendMessage(adminChatId, adminMessage)
            }
        }
    }
}