package com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview

class KeyboardButton(var plainText: String? = null,
                     var key: String = "",
                     var args: List<String> = emptyList(),
                     val requestLocation: Boolean = false,
                     val requestContact: Boolean = false)

fun button(key: String): KeyboardButton {
    return KeyboardButton(key = key)
}

fun plainTextButton(plainText: String): KeyboardButton {
    return KeyboardButton(plainText = plainText)
}