package com.sbrati.spring.boot.starter.kotlin.telegram.resolver

import com.sbrati.spring.boot.starter.kotlin.telegram.model.message.LocalizedMessage
import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.locale.TelegramLocaleResolver
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class TelegramMessageResolver(
    private val messageSource: MessageSource,
    private val localeResolver: TelegramLocaleResolver
) {

    fun resolve(chatId: Long, key: String, args: List<Any> = emptyList()): String {
        val locale = localeResolver.resolve(chatId)
        return resolve(key, args, locale)
    }

    private fun resolve(key: String, args: List<Any> = emptyList(), locale: Locale): String {
        val arguments = args.map { arg -> resolveOrMapToString(arg, locale) }.toTypedArray()
        return messageSource.getMessage(key, arguments, key, locale)!!
    }

    private fun resolveOrMapToString(arg: Any, locale: Locale): String {
        return if (arg is LocalizedMessage) {
            resolve(key = arg.key, args = arg.args, locale = locale)
        } else {
            arg.toString()
        }
    }
}