package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message

import com.github.kotlintelegrambot.entities.ChatId
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResult
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.getBotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotVoice
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.stereotype.Component

@Component
class BotVoiceHandler : BotHandler<BotVoice>(BotVoice::class.java) {

    override fun handle(botMessage: BotVoice): BotResult {
        val chatId = botMessage.chatId
        val result = bot.sendVoice(
            chatId = ChatId.fromId(chatId),
            audioId = botMessage.audioId,
            replyMarkup = botMessage.replyMarkup
        )

        val status = result.getCode()?.getBotResultStatus().orElse(BotResultStatus.OK)
        return BotResult(status, chatId)
    }
}