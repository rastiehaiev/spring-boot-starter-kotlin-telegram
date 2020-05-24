package com.sbrati.spring.boot.starter.kotlin.telegram.model

import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import java.util.*
import kotlin.collections.ArrayList

class TelegramSupportedLanguages(val defaultLanguage: Pair<String, Locale>, val otherLanguages: MutableList<Pair<String, Locale>> = ArrayList())

fun defaults(language: Pair<String, Locale>): TelegramSupportedLanguages = TelegramSupportedLanguages(language)

fun TelegramSupportedLanguages.then(language: Pair<String, Locale>): TelegramSupportedLanguages {
    otherLanguages.add(language)
    return this
}

fun TelegramSupportedLanguages.contains(language: String): Boolean {
    return localeByName(language) != null
}

fun TelegramSupportedLanguages.contains(locale: Locale): Boolean {
    return defaultLanguage.second.language == locale.language
            || otherLanguages.map { it.second }.firstOrNull { it.language == locale.language } != null
}

fun TelegramSupportedLanguages.localeByName(name: String): Locale? {
    return defaultLanguage.takeIf { it.first == name }?.second
            .orElse {
                otherLanguages.firstOrNull { it.first == name }?.second
            }
}