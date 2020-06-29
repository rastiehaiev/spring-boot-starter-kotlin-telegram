package com.sbrati.spring.boot.starter.kotlin.telegram.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.sbrati.spring.boot.starter.kotlin.telegram.component.BlockedChatHandler
import com.sbrati.spring.boot.starter.kotlin.telegram.component.GenericRequestLimiter
import com.sbrati.spring.boot.starter.kotlin.telegram.manager.TelegramManager
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BanOptions
import com.sbrati.spring.boot.starter.kotlin.telegram.properties.TelegramBotConfigurationProperties
import com.sbrati.spring.boot.starter.kotlin.telegram.properties.TelegramBotMode
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.InMemoryTelegramCommandExecutionContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.RequestStatisticsRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.repository.TelegramCommandExecutionContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.service.DefaultLocaleService
import com.sbrati.spring.boot.starter.kotlin.telegram.service.LocaleService
import com.sbrati.spring.boot.starter.kotlin.telegram.service.UserAwarenessService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import me.ivmg.telegram.Bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.callbackQuery
import me.ivmg.telegram.dispatcher.message
import me.ivmg.telegram.extensions.filters.Filter
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
        val bot = me.ivmg.telegram.bot {
            token = properties.token
            timeout = properties.timeoutSeconds
            logLevel = properties.logLevel
            dispatch {
                message(Filter.All) { _, update ->
                    manager.onUpdate(update)
                }
                callbackQuery { _, update ->
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
    open fun genericRequestLimiter(repository: RequestStatisticsRepository, banOptions: BanOptions): GenericRequestLimiter {
        return GenericRequestLimiter(banOptions, repository)
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