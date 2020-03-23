package com.sbrati.spring.boot.starter.kotlin.telegram.resolver.locale

import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandContextRepository
import com.sbrati.spring.boot.starter.kotlin.telegram.model.TelegramSupportedLanguages
import com.sbrati.spring.boot.starter.kotlin.telegram.service.LocaleService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class DefaultTelegramLocaleResolver(private val localeService: LocaleService?,
                                    private val supportedLanguages: TelegramSupportedLanguages,
                                    private val contextRepository: TelegramCommandContextRepository) : TelegramLocaleResolver {

    private val logger by LoggerDelegate()

    override fun resolve(chatId: Long): Locale {
        return localeService?.find(chatId)
                .orElse(contextRepository.findByChatId(chatId)?.progress?.locale)
                .orElse(supportedLanguages.defaultLanguage.second)
                .orElse(Locale.ENGLISH)
    }

    @PostConstruct
    private fun checkLocaleService() {
        if (localeService == null) {
            logger.warn("Please, implement ${LocaleService::class.qualifiedName} interface to normally resolve locales.")
        }
    }
}