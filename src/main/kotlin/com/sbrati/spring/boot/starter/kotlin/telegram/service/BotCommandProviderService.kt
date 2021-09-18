package com.sbrati.spring.boot.starter.kotlin.telegram.service

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotCommands
import com.sbrati.spring.boot.starter.kotlin.telegram.model.TelegramSupportedLanguages
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import java.util.*


class BotCommandProviderService(
    private val bot: Bot,
    private val botCommands: BotCommands,
    private val telegramMessageResolver: TelegramMessageResolver,
    private val telegramSupportedLanguages: TelegramSupportedLanguages,
) {

    private val logger by LoggerDelegate()

    @EventListener(ApplicationReadyEvent::class)
    fun setMyCommands() {
        logger.info("Setting my commands.")
        val languages = telegramSupportedLanguages.otherLanguages + telegramSupportedLanguages.defaultLanguage
        languages.map { it.second }.forEach { locale ->
            setMyCommands(locale = locale, languageCode = locale.language)
        }
        val defaultLocale = Locale.forLanguageTag(botCommands.defaultLanguage)
        setMyCommands(locale = defaultLocale)
    }

    private fun setMyCommands(languageCode: String? = null, locale: Locale) {
        bot.setMyCommands(languageCode, commands = botCommands.commands.map {
            BotCommand(it.command, telegramMessageResolver.resolve(
                key = it.descriptionKey,
                locale = locale
            ))
        })
    }
}