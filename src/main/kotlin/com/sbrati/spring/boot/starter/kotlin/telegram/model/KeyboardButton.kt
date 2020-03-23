package com.sbrati.spring.boot.starter.kotlin.telegram.model

class KeyboardButton(var plainText: String? = null,
                     var key: String = "",
                     var args: List<String> = emptyList())

fun button(key: String): KeyboardButton {
    return KeyboardButton(key = key)
}