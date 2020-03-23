package com.sbrati.spring.boot.starter.kotlin.telegram.resolver

import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.locale.TelegramLocaleResolver
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component

@Component
class TelegramMessageResolver(private val messageSource: MessageSource,
                              private val localeResolver: TelegramLocaleResolver) {

    fun resolve(chatId: Long, key: String, args: List<String> = emptyList()): String {
        val locale = localeResolver.resolve(chatId)
        return messageSource.getMessage(key, args.toTypedArray(), key, locale)!!
    }
}