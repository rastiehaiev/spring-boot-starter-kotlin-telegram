package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import com.github.kotlintelegrambot.entities.Update
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BanOptions

class BanHandler(private val handler: (Update, BanOptions) -> Any) {

    fun handle(update: Update, options: BanOptions): Any {
        return handler(update, options)
    }
}