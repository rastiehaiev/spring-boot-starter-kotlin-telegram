package com.sbrati.spring.boot.starter.kotlin.telegram.manager.resulthandler

import com.sbrati.spring.boot.starter.kotlin.telegram.bot.TelegramBot
import com.sbrati.spring.boot.starter.kotlin.telegram.model.ForwardMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.service.UserService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.stereotype.Component

@Component
class ForwardMessageHandler(private val telegramBot: TelegramBot, private val userService: UserService<*>?)
    : ResultHandler<ForwardMessage>(ForwardMessage::class.java) {

    private val logger by LoggerDelegate()

    override fun handle(chatId: Long, resultPayload: ForwardMessage) {
        if (userService == null) {
            logger.error("In order to use messages forward, consider to implement UserService interface.")
            return
        }
        val everyoneMessage = resultPayload.everyone
        everyoneMessage?.let {
            userService.getAllChatIds().forEach { telegramBot.sendMessage(it, everyoneMessage) }
        }

        val senderMessage = resultPayload.sender
        senderMessage?.let {
            telegramBot.sendMessage(chatId, senderMessage)
        }
    }
}