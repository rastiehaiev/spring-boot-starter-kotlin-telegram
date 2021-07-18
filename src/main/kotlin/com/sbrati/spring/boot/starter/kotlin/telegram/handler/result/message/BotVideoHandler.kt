package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message

import com.github.kotlintelegrambot.entities.ChatId
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResult
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.getBotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotVideo
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.stereotype.Component

@Component
class BotVideoHandler : BotHandler<BotVideo>(BotVideo::class.java) {

    override fun handle(botMessage: BotVideo): BotResult {
        val chatId = botMessage.chatId
        val result = bot.sendVideo(
            chatId = ChatId.fromId(chatId),
            fileId = botMessage.fileId,
            caption = botMessage.caption,
            replyMarkup = botMessage.replyMarkup
        )

        val status = result.getCode()?.getBotResultStatus().orElse(BotResultStatus.OK)
        return BotResult(status, chatId)
    }
}