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

fun days(): Keyboard = keyboard {
    row(
            plainTextButton("1"),
            plainTextButton("2"),
            plainTextButton("3"),
            plainTextButton("4"),
            plainTextButton("5")
    )
    row(
            plainTextButton("6"),
            plainTextButton("7"),
            plainTextButton("8"),
            plainTextButton("9"),
            plainTextButton("10")
    )
    row(
            plainTextButton("11"),
            plainTextButton("12"),
            plainTextButton("13"),
            plainTextButton("14"),
            plainTextButton("15")
    )
    row(
            plainTextButton("16"),
            plainTextButton("17"),
            plainTextButton("18"),
            plainTextButton("19"),
            plainTextButton("20")
    )
    row(
            plainTextButton("21"),
            plainTextButton("22"),
            plainTextButton("23"),
            plainTextButton("24"),
            plainTextButton("25")
    )
    row(
            plainTextButton("26"),
            plainTextButton("27"),
            plainTextButton("28"),
            plainTextButton("29"),
            plainTextButton("30")
    )
    row(plainTextButton("31"))
}