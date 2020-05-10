package com.sbrati.spring.boot.starter.kotlin.telegram.service

import java.util.*

interface LocaleService {

    fun findLocaleByChatId(chatId: Long): Locale?

    fun saveLocale(chatId: Long, locale: Locale)
}