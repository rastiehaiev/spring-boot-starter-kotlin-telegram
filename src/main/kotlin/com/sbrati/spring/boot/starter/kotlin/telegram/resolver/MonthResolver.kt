package com.sbrati.spring.boot.starter.kotlin.telegram.resolver

import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.locale.TelegramLocaleResolver
import com.sbrati.spring.boot.starter.kotlin.telegram.util.LoggerDelegate
import com.sbrati.spring.boot.starter.kotlin.telegram.util.orElse
import org.springframework.stereotype.Component
import java.time.Month
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.HashMap

@Component
class MonthResolver(private val localeResolver: TelegramLocaleResolver) {

    private val logger by LoggerDelegate()

    private val cache: MutableMap<String, Month> = HashMap()

    fun resolve(value: String, chatId: Long): Month? {
        val locale = localeResolver.resolve(chatId)
        val lowerCaseMonth = value.toLowerCase()
        if (lowerCaseMonth in cache) {
            return cache[lowerCaseMonth]!!
        }
        val month = findByDisplayName(locale, TextStyle.FULL_STANDALONE, lowerCaseMonth)
                .orElse(findByDisplayName(locale, TextStyle.FULL, lowerCaseMonth))
        if (month != null) {
            cache[lowerCaseMonth] = month
            logger.debug("Added pair ({} -> {}) to cache.", lowerCaseMonth, month)
        }
        return month
    }

    private fun findByDisplayName(locale: Locale, textStyle: TextStyle, value: String): Month? {
        return Month.values().asSequence()
                .associateBy { it.getDisplayName(textStyle, locale).toLowerCase() }
                .filter { (key, _) -> key == value }
                .map { (_, value) -> value }
                .firstOrNull()
    }
}