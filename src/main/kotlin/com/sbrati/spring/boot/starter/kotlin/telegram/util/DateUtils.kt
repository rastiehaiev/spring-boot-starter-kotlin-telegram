package com.sbrati.spring.boot.starter.kotlin.telegram.util

import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.*

fun toFormattedDate(locale: Locale, month: Month, day: Int, year: Int? = null): String {
    if (year == null) {
        return LocalDate.of(Year.now().value, month, day).format(DateTimeFormatter.ofPattern(nullableYearDateFormatPattern(locale), locale))
    }
    return LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern(dateFormatPattern(locale), locale))
}

private fun dateFormatPattern(locale: Locale): String {
    return if (locale == Locale.ENGLISH) {
        "MMMM d, yyyy"
    } else {
        "d MMMM, yyyy"
    }
}

private fun nullableYearDateFormatPattern(locale: Locale): String {
    return if (locale == Locale.ENGLISH) {
        "MMMM d"
    } else {
        "d MMMM"
    }
}