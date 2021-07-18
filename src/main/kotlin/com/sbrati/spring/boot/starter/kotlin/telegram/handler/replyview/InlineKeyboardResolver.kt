package com.sbrati.spring.boot.starter.kotlin.telegram.handler.replyview

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandExecutionContextProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview.InlineKeyboard
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import org.springframework.stereotype.Component

@Component
class InlineKeyboardResolver(
    private val telegramMessageResolver: TelegramMessageResolver,
    private val executionContextProvider: TelegramCommandExecutionContextProvider
) : AbstractReplyViewResolver<InlineKeyboard>(InlineKeyboard::class.java) {

    override fun handle(chatId: Long, resultPayload: InlineKeyboard): ReplyMarkup? {
        val buttons: List<List<InlineKeyboardButton>> = inlineKeyboardButtons(chatId, resultPayload)
        return InlineKeyboardMarkup.create(buttons)
    }

    private fun inlineKeyboardButtons(chatId: Long, inlineKeyboard: InlineKeyboard): List<List<InlineKeyboardButton>> {
        val context by lazy {
            executionContextProvider.findByChatId(chatId)
        }
        return inlineKeyboard.buttons.map { innerButtons ->
            innerButtons.map {
                var prefix = "GLOBAL"
                if (!it.global) {
                    prefix = context?.context?.id ?: ""
                }
                val text = telegramMessageResolver.resolve(chatId = chatId, key = it.key, args = it.args)
                val callbackData = it.callbackData?.let { data ->
                    "${prefix}^${data.asString()}"
                }
                if (it.url != null) {
                    InlineKeyboardButton.Url(text = text, url = it.url!!)
                } else {
                    InlineKeyboardButton.CallbackData(text = text, callbackData = callbackData!!)
                }
            }
        }
    }
}