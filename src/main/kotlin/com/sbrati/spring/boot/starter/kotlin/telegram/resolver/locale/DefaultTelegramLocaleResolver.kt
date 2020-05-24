package com.sbrati.spring.boot.starter.kotlin.telegram.resolver.locale

import com.sbrati.spring.boot.starter.kotlin.telegram.context.TelegramCommandExecutionContextProvider
import com.sbrati.spring.boot.starter.kotlin.telegram.model.TelegramSupportedLanguages
import com.sbrati.spring.boot.starter.kotlin.telegram.model.contains
import com.sbrati.spring.boot.starter.kotlin.telegram.service.LocaleService
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class DefaultTelegramLocaleResolver(private val localeService: LocaleService?,
                                    private val supportedLanguages: TelegramSupportedLanguages) : TelegramLocaleResolver {

    private val logger by LoggerDelegate()

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val executionContextProvider: TelegramCommandExecutionContextProvider by lazy {
        applicationContext.getBean(TelegramCommandExecutionContextProvider::class.java)
    }

    override fun resolve(chatId: Long): Locale {
        return localeService?.findLocaleByChatId(chatId)
                .orElse { executionContextProvider.findByChatId(chatId)?.context?.locale?.takeIf { supportedLanguages.contains(it) } }
                .orElse { supportedLanguages.defaultLanguage.second }
                .orElse(Locale.ENGLISH)
    }

    @PostConstruct
    private fun checkLocaleService() {
        if (localeService == null) {
            logger.warn("Please, implement ${LocaleService::class.qualifiedName} interface to normally resolve locales.")
        }
    }
}