package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result.message

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.network.Response
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResult
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.getBotResultStatus
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotSticker
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.stereotype.Component

@Component
class BotStickerHandler : BotHandler<BotSticker>(BotSticker::class.java) {

    override fun handle(botMessage: BotSticker): BotResult {
        val chatId = botMessage.chatId
        val result: Pair<retrofit2.Response<Response<Message>?>?, Exception?> = bot.sendSticker(
            chatId = ChatId.fromId(chatId),
            sticker = botMessage.sticker,
            replyMarkup = botMessage.replyMarkup,
            replyToMessageId = botMessage.replyToMessageId,
            disableNotification = botMessage.disableNotification,
        )

        val status = result.getCode()?.getBotResultStatus().orElse(BotResultStatus.OK)
        return BotResult(status, chatId)
    }
}