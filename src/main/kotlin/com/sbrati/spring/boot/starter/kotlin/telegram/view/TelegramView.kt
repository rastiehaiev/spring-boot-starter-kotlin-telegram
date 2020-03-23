package com.sbrati.spring.boot.starter.kotlin.telegram.view

import com.sbrati.spring.boot.starter.kotlin.telegram.model.KeyboardButton
import com.sbrati.spring.boot.starter.kotlin.telegram.model.TelegramSupportedLanguages
import org.springframework.stereotype.Component

@Component
class TelegramView(private val supportedLanguages: TelegramSupportedLanguages) {

    fun supportedLanguagesButtons(): List<KeyboardButton> {
        val defaultButton = KeyboardButton(plainText = supportedLanguages.defaultLanguage.first)
        val otherButtons = supportedLanguages.otherLanguages.map { KeyboardButton(plainText = it.first) }
        return mutableListOf(defaultButton).also { it.addAll(otherButtons) }
    }
}