package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.StickerMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotSticker
import org.springframework.stereotype.Component

@Component
class StickerMessageHandler : AbstractMessageHandler<StickerMessage>(StickerMessage::class.java) {

    override fun messages(chatId: Long, message: StickerMessage): List<BotResultEntity> {
        val replyMarkup = replyViewResolver.resolve(chatId, message.replyView)
        return listOf(BotSticker(
            chatId = chatId,
            sticker = message.sticker,
            disableNotification = message.disableNotification,
            replyToMessageId = message.replyToMessageId,
            replyMarkup = replyMarkup
        ))
    }
}