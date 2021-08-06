package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.VideoMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotResultEntity
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.BotVideo
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.stereotype.Component

@Component
class VideoMessageHandler(private val telegramMessageResolver: TelegramMessageResolver) :
    AbstractMessageHandler<VideoMessage>(VideoMessage::class.java) {

    override fun messages(chatId: Long, message: VideoMessage): List<BotResultEntity> {
        val replyMarkup = replyViewResolver.resolve(chatId, message.replyView)
        val caption = if (message.captionKey.isNotBlank()) {
            telegramMessageResolver.resolve(
                chatId = chatId,
                key = message.captionKey,
                args = message.captionArgs
            )
        } else null
        return listOf(
            BotVideo(
                chatId = chatId,
                caption = caption,
                parseMode = message.parseMode,
                replyMarkup = replyMarkup
            )
        )
    }
}