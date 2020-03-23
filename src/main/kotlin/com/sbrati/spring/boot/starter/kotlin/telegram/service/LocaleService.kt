package com.sbrati.spring.boot.starter.kotlin.telegram.service

import java.util.*

interface LocaleService {

    fun find(chatId: Long): Locale?

    fun save(chatId: Long, locale: Locale)
}