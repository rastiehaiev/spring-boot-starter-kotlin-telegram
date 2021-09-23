package com.sbrati.spring.boot.starter.kotlin.telegram.command.impl

import com.github.kotlintelegrambot.entities.ParseMode
import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommand
import com.sbrati.spring.boot.starter.kotlin.telegram.model.TelegramSupportedLanguages
import com.sbrati.spring.boot.starter.kotlin.telegram.model.allLanguages
import com.sbrati.spring.boot.starter.kotlin.telegram.model.finish
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.EmptyMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.message
import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.plainMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.model.route
import com.sbrati.spring.boot.starter.kotlin.telegram.service.UserService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate


class NotifyAllCommand(
    private val userService: UserService<*>,
    private val supportedLanguages: TelegramSupportedLanguages,
) : TelegramCommand<TextPerLocaleContext>(
    name = "notifyall",
    contextType = TextPerLocaleContext::class.java,
    admin = true,
    synthetic = false,
) {

    private val log by LoggerDelegate()

    init {
        apply {
            stage("notifyall") {
                start { _, context ->
                    val language = supportedLanguages.defaultLanguage.second.language
                    context.currentLanguage = language
                    message {
                        key = "notifyall.info.message.$language"
                        parseMode = ParseMode.MARKDOWN
                    }
                }
                update { update, context ->
                    val updateText = update.message?.text ?: return@update EmptyMessage
                    val currentLanguage = context.currentLanguage
                    context.textPerLocaleMap[currentLanguage] = updateText
                    val newCurrentLanguage = supportedLanguages.allLanguages()
                        .zipWithNext()
                        .find { (left, _) -> left.second.language == currentLanguage }
                        ?.second?.second?.language

                    if (newCurrentLanguage != null) {
                        context.currentLanguage = newCurrentLanguage
                        message {
                            key = "notifyall.info.message.$newCurrentLanguage"
                            parseMode = ParseMode.MARKDOWN
                        }
                    } else {
                        finish {
                            route {
                                everyone { chatId ->
                                    val locale = userService.findLocaleByChatId(chatId)
                                        ?: supportedLanguages.defaultLanguage.second
                                    log.info("Sending message to user $chatId. Locale: ${locale.language}.")
                                    plainMessage {
                                        text = context.textPerLocaleMap[locale.language]
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class TextPerLocaleContext : Context() {
    var currentLanguage: String = ""
    val textPerLocaleMap = mutableMapOf<String, String>()
}
