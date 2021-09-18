package com.sbrati.spring.boot.starter.kotlin.telegram.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.preCheckoutQuery
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.logging.LogLevel
import com.sbrati.spring.boot.starter.kotlin.telegram.component.BlockedChatHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BotCommands
import com.sbrati.spring.boot.starter.kotlin.telegram.component.GenericRequestLimiter
import com.sbrati.spring.boot.starter.kotlin.telegram.manager.TelegramManager
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BanOptions
import com.sbrati.spring.boot.starter.kotlin.telegram.model.TelegramSupportedLanguages
import com.sbrati.spring.boot.starter.kotlin.telegram.properties.TelegramBotConfigurationProperties
import com.sbrati.spring.boot.starter.kotlin.telegram.properties.TelegramBotMode
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.InMemoryTelegramCommandExecutionContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.RequestStatisticsRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.TelegramCommandExecutionContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.TelegramMessageResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.service.BotCommandProviderService
import com.sbrati.spring.boot.starter.kotlin.telegram.service.DefaultLocaleService
import com.sbrati.spring.boot.starter.kotlin.telegram.service.LocaleService
import com.sbrati.spring.boot.starter.kotlin.telegram.service.UserAwarenessService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
@EnableConfigurationProperties(TelegramBotConfigurationProperties::class)
@ComponentScan(value = ["com.sbrati.spring.boot.starter.kotlin.telegram"])
open class TelegramBotConfiguration(private val properties: TelegramBotConfigurationProperties) {

    private val logger by LoggerDelegate()

    @Autowired
    private var userAwarenessService: UserAwarenessService? = null

    @Bean
    open fun internalBot(manager: TelegramManager): Bot {
        val botLogLevel = when (properties.logLevel?.toUpperCase()) {
            "BASIC" -> LogLevel.Network.Basic
            "BODY" -> LogLevel.Network.Body
            "HEADERS" -> LogLevel.Network.Headers
            else -> LogLevel.Network.None
        }

        val bot = com.github.kotlintelegrambot.bot {
            token = properties.token
            timeout = properties.timeoutSeconds
            logLevel = botLogLevel
                dispatch {
                    message(Filter.All) {
                        manager.onUpdate(update)
                    }
                    callbackQuery {
                        manager.onUpdate(update)
                    }
                    preCheckoutQuery {
                        manager.onUpdate(update)
                    }
                }
        }
        logger.info("Created Telegram Bot. Mode: ${properties.mode}.")
        return bot
    }

    @Bean
    open fun commandLineRunner(internalBot: Bot, objectMapper: ObjectMapper): CommandLineRunner {
        return CommandLineRunner {
            objectMapper.registerModule(KotlinModule())
            if (properties.mode == TelegramBotMode.POLLING) {
                logger.info("Starting polling updates.")
                internalBot.startPolling()
            }
        }
    }

    @Bean(name = ["messageSource"])
    open fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }

    @Bean
    @ConditionalOnMissingBean(LocaleService::class)
    open fun defaultLocaleService(): LocaleService {
        logger.warn("Default locale service has been created. This means user preferred locales will be stored in memory.")
        return DefaultLocaleService()
    }

    @Bean
    @ConditionalOnMissingBean(TelegramCommandExecutionContextRepository::class)
    open fun commandExecutionContextRepository(): TelegramCommandExecutionContextRepository {
        logger.warn("Default TelegramCommandExecutionContextRepository has been created. This means command context will be stored in memory.")
        return InMemoryTelegramCommandExecutionContextRepository()
    }

    @Bean
    @ConditionalOnMissingBean(BlockedChatHandler::class)
    open fun blockedChatHandler(): BlockedChatHandler {
        return object : BlockedChatHandler {
            override fun onChatIdBlocked(chatId: Long) {
                logger.warn("Chat with ID=$chatId has been blocked.")
            }
        }
    }

    @Bean
    @ConditionalOnBean(RequestStatisticsRepository::class)
    open fun genericRequestLimiter(
        repository: RequestStatisticsRepository,
        banOptions: BanOptions
    ): GenericRequestLimiter {
        return GenericRequestLimiter(banOptions, repository)
    }

    @Bean
    @ConditionalOnBean(BotCommands::class)
    open fun botCommandProviderService(
        bot: Bot,
        telegramMessageResolver: TelegramMessageResolver,
        telegramSupportedLanguages: TelegramSupportedLanguages,
        botCommands: BotCommands,
    ): BotCommandProviderService {
        return BotCommandProviderService(bot, botCommands, telegramMessageResolver, telegramSupportedLanguages)
    }

    @EventListener(ApplicationReadyEvent::class)
    open fun userAware() {
        userAwarenessService?.let {
            logger.info("Users aware processing has been started.")
            it.aware()
            logger.info("Users aware processing has been finished.")
        }
    }
}