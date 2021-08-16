package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.sbrati.spring.boot.starter.kotlin.telegram.component.AdminChatIdsProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.handler.MessageHandlers
import com.sbrati.spring.boot.starter.kotlin.telegram.model.RouteMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.service.UserService
import org.springframework.stereotype.Component

@Component
class RouteMessageHandler(
    private val userService: UserService<*>?,
    private val adminChatIdsProvider: AdminChatIdsProvider?,
    private val messageHandlers: MessageHandlers,
) : ResultHandler<RouteMessage>(RouteMessage::class.java) {

    override fun handle(chatId: Long, resultPayload: RouteMessage) {
        val adminsMessage = resultPayload.adminsMessage
        adminsMessage?.let {
            adminChatIdsProvider?.adminChatIds()?.forEach { messageHandlers.processMessage(it, adminsMessage) }
        }
        val everyoneMessage = resultPayload.everyoneMessage
        everyoneMessage?.let {
            userService?.getAllChatIds()?.forEach { messageHandlers.processMessage(it, everyoneMessage) }
        }
        val receiverMessage = resultPayload.receiverMessage
        val receiverChatId = resultPayload.receiverChatId
        receiverMessage?.let {
            receiverChatId?.let {
                messageHandlers.processMessage(receiverChatId, receiverMessage)
            }
        }
        val senderMessage = resultPayload.senderMessage
        senderMessage?.let {
            messageHandlers.processMessage(chatId, senderMessage)
        }
    }
}