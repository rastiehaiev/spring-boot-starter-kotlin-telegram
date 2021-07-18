package com.sbrati.spring.boot.starter.kotlin.telegram.handler.update

import com.github.kotlintelegrambot.entities.Update
import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context

class WholeUpdateHandler<T : Context>(private val handler: (Update, T) -> Any) : UpdateHandler<T>() {

    override fun isApplicable(update: Update, progress: T): Boolean {
        return true
    }

    override fun handle(update: Update, progress: T): Any {
        return handler(update, progress)
    }
}