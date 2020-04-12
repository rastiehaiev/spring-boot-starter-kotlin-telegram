package com.sbrati.spring.boot.starter.kotlin.telegram.handler

import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandProgress
import me.ivmg.telegram.entities.Update

class WholeUpdateHandler<T : TelegramCommandProgress>(private val handler: (Update, T) -> Any) : UpdateHandler<T>() {

    override fun isApplicable(update: Update, progress: T): Boolean {
        return true
    }

    override fun handle(update: Update, progress: T): Any {
        return handler(update, progress)
    }
}