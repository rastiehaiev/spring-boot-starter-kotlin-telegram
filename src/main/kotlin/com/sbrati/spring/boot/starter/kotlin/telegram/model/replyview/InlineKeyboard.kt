package com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview

class InlineKeyboard : ReplyView {

    val buttons: MutableList<List<InlineKeyboardButton>> = ArrayList()

    fun row(vararg buttons: InlineKeyboardButton) {
        this.buttons.add(buttons.toList())
    }
}

fun inlineKeyboard(builder: InlineKeyboard.() -> Unit): InlineKeyboard {
    return InlineKeyboard().apply(builder)
}