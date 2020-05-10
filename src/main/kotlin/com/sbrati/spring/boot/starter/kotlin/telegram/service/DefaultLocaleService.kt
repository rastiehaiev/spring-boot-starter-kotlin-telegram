package com.sbrati.spring.boot.starter.kotlin.telegram.service

import java.util.*
import kotlin.collections.HashMap

class DefaultLocaleService(private val localesMap: MutableMap<Long, Locale> = HashMap()) : LocaleService {

    override fun findLocaleByChatId(chatId: Long): Locale? {
        return localesMap[chatId]
    }

    override fun saveLocale(chatId: Long, locale: Locale) {
        localesMap[chatId] = locale
    }
}