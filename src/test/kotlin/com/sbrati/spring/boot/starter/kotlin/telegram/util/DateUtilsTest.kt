package com.sbrati.spring.boot.starter.kotlin.telegram.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Month
import java.util.*

internal class DateUtilsTest {

    @Test
    fun `Should resolve date with year in Russian locale`() {
        assertEquals("12 сентября, 2020", toFormattedDate(month = Month.SEPTEMBER, day = 12, locale = Locale("ru"), year = 2020))
    }

    @Test
    fun `Should resolve date without year in Russian locale`() {
        assertEquals("12 ноября", toFormattedDate(month = Month.NOVEMBER, day = 12, locale = Locale("ru")))
    }

    @Test
    fun `Should resolve date with year in Ukrainian locale`() {
        assertEquals("1 березня, 2020", toFormattedDate(month = Month.MARCH, day = 1, locale = Locale("uk"), year = 2020))
    }

    @Test
    fun `Should resolve date without year in Ukrainian locale`() {
        assertEquals("17 серпня", toFormattedDate(month = Month.AUGUST, day = 17, locale = Locale("uk")))
    }

    @Test
    fun `Should resolve date with year in English locale`() {
        assertEquals("March 1, 2021", toFormattedDate(month = Month.MARCH, day = 1, locale = Locale("en"), year = 2021))
    }

    @Test
    fun `Should resolve date without year in English locale`() {
        assertEquals("August 17", toFormattedDate(month = Month.AUGUST, day = 17, locale = Locale("en")))
    }
}