package com.sbrati.spring.boot.starter.kotlin.telegram.model.replyview

class Keyboard(val buttons: List<List<KeyboardButton>>, val resize: Boolean = true) : ReplyView {

    constructor(builder: Builder) : this(builder.buttons, builder.resize)

    class Builder {

        val buttons: MutableList<List<KeyboardButton>> = ArrayList()
        var resize: Boolean = true

        fun row(buttons: List<KeyboardButton>) {
            this.buttons.add(buttons)
        }

        fun row(vararg buttons: KeyboardButton) {
            this.buttons.add(buttons.toList())
        }
    }
}

fun keyboard(builder: Keyboard.Builder.() -> Unit): Keyboard {
    return Keyboard(Keyboard.Builder().apply(builder))
}

fun months(): Keyboard = keyboard {
    row(button("month.january"), button("month.february"), button("month.march"))
    row(button("month.april"), button("month.may"), button("month.june"))
    row(button("month.july"), button("month.august"), button("month.september"))
    row(button("month.october"), button("month.november"), button("month.december"))
}