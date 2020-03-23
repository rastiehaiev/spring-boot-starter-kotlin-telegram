package com.sbrati.spring.boot.starter.kotlin.telegram.service

import com.sbrati.spring.boot.starter.kotlin.telegram.model.TelegramSupportedLanguages
import com.sbrati.spring.boot.starter.kotlin.telegram.model.contains
import com.sbrati.spring.boot.starter.kotlin.telegram.model.localeByName
import org.springframework.stereotype.Service

@Service
class LocaleSettingsService(private val localeService: LocaleService, private val supportedLanguages: TelegramSupportedLanguages) {

    fun isLanguageSupported(language: String): Boolean {
        return supportedLanguages.contains(language)
    }

    fun updateUserLanguagePreferences(chatId: Long, language: String) {
        val locale = supportedLanguages.localeByName(language)!!
        localeService.save(chatId, locale)
    }
}