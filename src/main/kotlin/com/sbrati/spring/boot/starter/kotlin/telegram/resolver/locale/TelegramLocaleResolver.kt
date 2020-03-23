package com.sbrati.spring.boot.starter.kotlin.telegram.resolver.locale

import java.util.*

interface TelegramLocaleResolver {

    fun resolve(chatId: Long): Locale
}