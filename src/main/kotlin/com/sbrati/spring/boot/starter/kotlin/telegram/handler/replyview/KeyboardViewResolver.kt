package com.sbrati.spring.boot.starter.kotlin.telegram.handler.replyview

import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.Keyboard
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.stereotype.Component

@Component
class KeyboardViewResolver(private val telegramMessageResolver: TelegramMessageResolver)
    : AbstractReplyViewResolver<Keyboard>(Keyboard::class.java) {

    override fun handle(chatId: Long, resultPayload: Keyboard): ReplyMarkup? {
        val apiButtons = resultPayload.buttons.map { convertToApiButtons(chatId, it) }
        return KeyboardReplyMarkup(keyboard = apiButtons, resizeKeyboard = resultPayload.resize, oneTimeKeyboard = resultPayload.oneTime)
    }

    private fun convertToApiButtons(chatId: Long, buttons: List<com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.KeyboardButton>): List<KeyboardButton> {
        return buttons.map {
            KeyboardButton(text = getTextFromKeyboardButton(chatId, it.key, it.args, it.plainText),
                    requestContact = it.requestContact,
                    requestLocation = it.requestLocation)
        }
    }

    private fun getTextFromKeyboardButton(chatId: Long, key: String, args: List<String>, plainText: String?): String {
        if (plainText != null) {
            return plainText
        }
        return telegramMessageResolver.resolve(chatId = chatId, key = key, args = args)
    }
}