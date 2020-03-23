package com.sbrati.spring.boot.starter.kotlin.telegram.resolver

import com.sbrati.spring.boot.starter.kotlin.telegram.resolver.locale.TelegramLocaleResolver
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Month
import java.util.*

@ExtendWith(MockKExtension::class)
internal class MonthResolverTest {

    private val chatId = 1L

    private val localeResolver: TelegramLocaleResolver = mockk()

    private val monthResolver: MonthResolver = MonthResolver(localeResolver)

    @Test
    fun `Should resolve February month in Russian locale by lowercase string`() {
        every { localeResolver.resolve(chatId) } returns Locale("ru")

        assertEquals(Month.FEBRUARY, monthResolver.resolve(chatId = chatId, value = "февраль"))
    }

    @Test
    fun `Should resolve February month in Russian locale by capitalized string`() {
        every { localeResolver.resolve(chatId) } returns Locale("ru")

        assertEquals(Month.FEBRUARY, monthResolver.resolve(chatId = chatId, value = "февраль"))
    }

    @Test
    fun `Should resolve April month in Russian locale by uppercase string`() {
        every { localeResolver.resolve(chatId) } returns Locale("ru")

        assertEquals(Month.APRIL, monthResolver.resolve(chatId = chatId, value = "апрель"))
    }

    @Test
    fun `Should resolve March month in English locale by lowercase string`() {
        every { localeResolver.resolve(chatId) } returns Locale("en")

        assertEquals(Month.MARCH, monthResolver.resolve(chatId = chatId, value = "march"))
    }

    @Test
    fun `Should resolve March month in English locale by capitalized string`() {
        every { localeResolver.resolve(chatId) } returns Locale("en")

        assertEquals(Month.MARCH, monthResolver.resolve(chatId = chatId, value = "March"))
    }

    @Test
    fun `Should resolve June month in English locale by uppercase string`() {
        every { localeResolver.resolve(chatId) } returns Locale("en")

        assertEquals(Month.JUNE, monthResolver.resolve(chatId = chatId, value = "JUNE"))
    }

    @Test
    fun `Should resolve August month in Ukrainian locale by lowercase string`() {
        every { localeResolver.resolve(chatId) } returns Locale("uk")

        assertEquals(Month.AUGUST, monthResolver.resolve(chatId = chatId, value = "серпень"))
    }

    @Test
    fun `Should resolve September month in Ukrainian locale by capitalized string`() {
        every { localeResolver.resolve(chatId) } returns Locale("uk")

        assertEquals(Month.SEPTEMBER, monthResolver.resolve(chatId = chatId, value = "Вересень"))
    }

    @Test
    fun `Should resolve October month in Ukrainian locale by uppercase string`() {
        every { localeResolver.resolve(chatId) } returns Locale("uk")

        assertEquals(Month.OCTOBER, monthResolver.resolve(chatId = chatId, value = "ЖОВТЕНЬ"))
    }
}