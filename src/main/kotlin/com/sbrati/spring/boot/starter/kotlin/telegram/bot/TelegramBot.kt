package com.sbrati.spring.boot.starter.kotlin.telegram.bot

import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.model.AnswerCallbackQuery
import com.sbrati.spring.boot.starter.kotlin.telegram.model.InlineKeyboard
import com.sbrati.spring.boot.starter.kotlin.telegram.model.Keyboard
import com.sbrati.spring.boot.starter.kotlin.telegram.model.Message
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.*
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class TelegramBot(private val applicationContext: ApplicationContext,
                  private val telegramMessageResolver: TelegramMessageResolver,
                  private val contextRepository: TelegramCommandContextRepository) {

    private val bot: Bot by lazy {
        applicationContext.getBean(Bot::class.java)
    }

    fun answerCallbackQuery(chatId: Long, answerCallbackQuery: AnswerCallbackQuery) {
        if (answerCallbackQuery.key.isNotBlank()) {
            val text = telegramMessageResolver.resolve(chatId = chatId, key = answerCallbackQuery.key, args = answerCallbackQuery.args)
            bot.answerCallbackQuery(cacheTime = 20, callbackQueryId = answerCallbackQuery.callbackQueryId, text = text, showAlert = answerCallbackQuery.showAlert)
        }
    }

    fun sendMessage(chatId: Long, message: Message) {
        if (message.key.isNotBlank()) {
            val replyMarkup: ReplyMarkup = keyboardMarkup(chatId, message.keyboard)
                    .orElse(inlineKeyboardMarkup(chatId, message.inlineKeyboard))
                    .orElse(ReplyKeyboardRemove(removeKeyboard = true))

            val text = telegramMessageResolver.resolve(chatId = chatId, key = message.key, args = message.args)
            bot.sendMessage(replyMarkup = replyMarkup, chatId = chatId, text = text, parseMode = message.parseMode)
        }
    }

    private fun keyboardMarkup(chatId: Long, keyboard: Keyboard?): KeyboardReplyMarkup? {
        if (keyboard == null) {
            return null
        }
        val apiButtons = keyboard.buttons.map { convertToApiButtons(chatId, it) }
        return KeyboardReplyMarkup(keyboard = apiButtons, resizeKeyboard = keyboard.resize)
    }

    private fun convertToApiButtons(chatId: Long, buttons: List<com.sbrati.spring.boot.starter.kotlin.telegram.model.KeyboardButton>): List<KeyboardButton> {
        return buttons.map { KeyboardButton(text = getTextFromKeyboardButton(chatId, it.key, it.args, it.plainText)) }
    }

    private fun getTextFromKeyboardButton(chatId: Long, key: String, args: List<String>, plainText: String?): String {
        if (plainText != null) {
            return plainText
        }
        return telegramMessageResolver.resolve(chatId = chatId, key = key, args = args)
    }

    private fun inlineKeyboardMarkup(chatId: Long, inlineKeyboard: InlineKeyboard?): InlineKeyboardMarkup? {
        if (inlineKeyboard != null) {
            val buttons: List<List<InlineKeyboardButton>> = inlineKeyboardButtons(chatId, inlineKeyboard)
            return InlineKeyboardMarkup(buttons)
        }
        return null
    }

    private fun inlineKeyboardButtons(chatId: Long, inlineKeyboard: InlineKeyboard): List<List<InlineKeyboardButton>> {
        val context by lazy {
            contextRepository.findByChatId(chatId)!!
        }
        return inlineKeyboard.buttons.map { innerButtons ->
            innerButtons.map {
                var prefix = "GLOBAL"
                if (!it.global) {
                    prefix = "${context.progress!!.uuid}"
                }
                val text = telegramMessageResolver.resolve(chatId = chatId, key = it.key, args = it.args)
                InlineKeyboardButton(text = text, callbackData = "${prefix}^${it.callbackData.asString()}")
            }
        }
    }
}