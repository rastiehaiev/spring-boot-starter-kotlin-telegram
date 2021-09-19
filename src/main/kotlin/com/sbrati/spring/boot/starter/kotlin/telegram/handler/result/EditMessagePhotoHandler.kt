package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.entities.inputmedia.InputMediaPhoto
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.EditMessagePhoto
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.ReplyViewResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class EditMessagePhotoHandler(
    private val telegramMessageResolver: TelegramMessageResolver,
) : ResultHandler<EditMessagePhoto>(EditMessagePhoto::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var replyViewResolver: ReplyViewResolver

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: EditMessagePhoto) {
        val replyMarkup = replyViewResolver.resolve(chatId, resultPayload.replyView)
        val caption = if (resultPayload.captionKey.isNotBlank()) {
            telegramMessageResolver.resolve(
                chatId = chatId,
                key = resultPayload.captionKey,
                args = resultPayload.captionArgs
            )
        } else null
        bot.editMessageMedia(
            chatId = ChatId.fromId(chatId),
            messageId = resultPayload.messageId,
            media = InputMediaPhoto(
                caption = caption,
                media = TelegramFile.ByFileId(resultPayload.photoFileId)
            ),
            replyMarkup = replyMarkup
        )
    }
}