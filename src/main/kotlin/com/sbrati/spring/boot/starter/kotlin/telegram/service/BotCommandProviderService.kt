package com.sbrati.spring.boot.starter.kotlin.telegram.service

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.BotCommand
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotCommands
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import java.util.*


class BotCommandProviderService(
    private val bot: Bot,
    private val botCommands: BotCommands,
    private val telegramMessageResolver: TelegramMessageResolver,
) {

    private val logger by LoggerDelegate()

    @EventListener(ApplicationReadyEvent::class)
    fun setCommands() {
        logger.info("Setting my commands.")
        botCommands.commands.forEach { (languageCode, commands) ->
            val botCommands = commands.map {
                BotCommand(it.command, telegramMessageResolver.resolve(
                    key = it.descriptionKey,
                    locale = Locale.forLanguageTag(languageCode)
                ))
            }
            bot.setMyCommands(languageCode, botCommands)
        }
    }
}