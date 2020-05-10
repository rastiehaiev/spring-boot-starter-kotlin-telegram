package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResult
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.getBotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import me.ivmg.telegram.entities.Message
import me.ivmg.telegram.network.Response
import org.springframework.stereotype.Component

@Component
class BotMessageHandler: BotHandler<BotMessage>(BotMessage::class.java) {

    override fun handle(botMessage: BotMessage): BotResult {
        val chatId = botMessage.chatId
        val result: Pair<retrofit2.Response<Response<Message>?>?, Exception?> = bot.sendMessage(chatId = chatId,
                text = botMessage.text,
                parseMode = botMessage.parseMode,
                replyMarkup = botMessage.replyMarkup)

        val status = result.getCode()?.getBotResultStatus().orElse(BotResultStatus.OK)
        return BotResult(status, chatId)
    }
}