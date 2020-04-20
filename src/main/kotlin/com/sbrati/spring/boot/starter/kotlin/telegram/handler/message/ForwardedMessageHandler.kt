package com.sbrati.spring.boot.starter.kotlin.telegram.handler.message

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.ForwardedMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.bot.*
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.util.chatId
import com.sbrati.spring.boot.starter.kotlin.telegram.util.fullName
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.stereotype.Component

@Component
class ForwardedMessageHandler(private val telegramMessageResolver: TelegramMessageResolver)
    : AbstractMessageHandler<ForwardedMessage>(ForwardedMessage::class.java) {

    private fun args(text: String, resultPayload: ForwardedMessage): MutableList<String> {
        val argsList = mutableListOf(text)
        val incomingArgs = resultPayload.args
        if (incomingArgs != null) {
            argsList.addAll(incomingArgs)
        }
        return argsList
    }

    override fun messages(chatId: Long, message: ForwardedMessage): List<BotResultEntity> {
        val update = message.original!!
        val userFullName = update.fullName()
        val caption = update.message?.caption
        val replyMarkup = replyMarkup(chatId, message.replyView)
        val results: MutableList<BotResultEntity> = arrayListOf()

        val photoList = update.message?.photo
        photoList?.maxBy { it.width }?.let {
            val userChatId = update.chatId()
            val userLink = "[$userFullName](tg://user?id=$userChatId)"
            val resultCaption = userLink + (caption?.prependIndent(" :: ").orElse(""))
            results.add(BotPhoto(chatId = chatId, photo = it.fileId, caption = resultCaption, replyMarkup = replyMarkup))
        }

        val voice = update.message?.voice
        voice?.let {
            results.add(BotVoice(chatId = chatId, audioId = voice.fileId, replyMarkup = replyMarkup))
        }

        val videoNote = update.message?.videoNote
        videoNote?.let {
            results.add(BotVideoNote(chatId = chatId, videoNoteId = videoNote.fileId, replyMarkup = replyMarkup))
        }

        val video = update.message?.video
        video?.let {
            results.add(BotVideo(chatId = chatId, fileId = video.fileId, caption = caption, replyMarkup = replyMarkup))
        }

        val text = update.message?.text
        if (text != null) {
            val textWrapperKey = message.textWrapperKey
            if (textWrapperKey != null) {
                val resultText = telegramMessageResolver.resolve(chatId, key = textWrapperKey, args = args(text, message))
                results.add(BotMessage(chatId = chatId, text = resultText, parseMode = message.parseMode, replyMarkup = replyMarkup))
            } else {
                results.add(BotMessage(chatId = chatId, text = text, parseMode = message.parseMode, replyMarkup = replyMarkup))
            }
        }
        return results
    }
}