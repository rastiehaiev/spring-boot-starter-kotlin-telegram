package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.VideoMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.ReplyViewResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class VideoMessageHandler(
    private val telegramMessageResolver: TelegramMessageResolver
) : ResultHandler<VideoMessage>(VideoMessage::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var replyViewResolver: ReplyViewResolver

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: VideoMessage) {
        val replyMarkup = replyViewResolver.resolve(chatId, resultPayload.replyView)
        val caption = telegramMessageResolver.resolve(
            chatId = chatId,
            key = resultPayload.captionKey,
            args = resultPayload.captionArgs
        )

        bot.sendVideo(
            chatId = ChatId.fromId(chatId),
            caption = caption,
            replyMarkup = replyMarkup,
            fileId = resultPayload.fileId
        )
    }
}