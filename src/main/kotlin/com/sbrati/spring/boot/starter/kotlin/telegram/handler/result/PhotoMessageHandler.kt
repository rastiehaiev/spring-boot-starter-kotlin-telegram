package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.PhotoMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotPhoto
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.stereotype.Component

@Component
class PhotoMessageHandler(private val telegramMessageResolver: TelegramMessageResolver) :
    AbstractMessageHandler<PhotoMessage>(PhotoMessage::class.java) {

    override fun messages(chatId: Long, message: PhotoMessage): List<BotResultEntity> {
        val replyMarkup = replyViewResolver.resolve(chatId, message.replyView)
        val caption = if (message.captionKey.isNotBlank()) {
            telegramMessageResolver.resolve(
                chatId = chatId,
                key = message.captionKey,
                args = message.captionArgs
            )
        } else null
        return listOf(
            BotPhoto(
                chatId = chatId,
                caption = caption,
                photo = message.file,
                fileId = message.fileId,
                replyMarkup = replyMarkup
            )
        )
    }
}