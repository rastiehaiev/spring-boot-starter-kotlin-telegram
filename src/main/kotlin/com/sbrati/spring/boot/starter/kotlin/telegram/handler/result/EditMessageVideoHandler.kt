package com.sbrati.spring.boot.starter.kotlin.telegram.handler.result

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.entities.inputmedia.InputMediaVideo
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.EditMessageVideo
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.ReplyViewResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class EditMessageVideoHandler : ResultHandler<EditMessageVideo>(EditMessageVideo::class.java) {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var replyViewResolver: ReplyViewResolver

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    override fun handle(chatId: Long, resultPayload: EditMessageVideo) {
        val replyMarkup = replyViewResolver.resolve(chatId, resultPayload.replyView)
        bot.editMessageMedia(
            chatId = ChatId.fromId(chatId),
            messageId = resultPayload.messageId,
            media = InputMediaVideo(
                media = TelegramFile.ByFileId(resultPayload.videoFileId)
            ),
            replyMarkup = replyMarkup
        )
    }
}