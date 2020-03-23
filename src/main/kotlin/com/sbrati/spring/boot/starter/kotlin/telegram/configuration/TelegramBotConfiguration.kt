package com.sbrati.spring.boot.starter.kotlin.telegram.configuration

import com.sbrati.spring.boot.starter.kotlin.telegram.manager.TelegramManager
import com.sbrati.spring.boot.starter.kotlin.telegram.properties.TelegramBotConfigurationProperties
import com.sbrati.spring.boot.starter.kotlin.telegram.properties.TelegramBotMode
import com.sbrati.spring.boot.starter.kotlin.telegram.service.DefaultLocaleService
import com.sbrati.spring.boot.starter.kotlin.telegram.service.LocaleService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import me.ivmg.telegram.Bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.callbackQuery
import me.ivmg.telegram.dispatcher.message
import me.ivmg.telegram.extensions.filters.Filter
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
@EnableConfigurationProperties(TelegramBotConfigurationProperties::class)
@ComponentScan(value = ["com.sbrati.spring.boot.starter.kotlin.telegram"])
open class TelegramBotConfiguration(private val properties: TelegramBotConfigurationProperties) {

    private val logger by LoggerDelegate()

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
    open fun pollTelegramUpdates(internalBot: Bot): CommandLineRunner {
        return CommandLineRunner {
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
}