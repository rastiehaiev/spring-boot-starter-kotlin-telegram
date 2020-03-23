package com.sbrati.spring.boot.starter.kotlin.telegram.handler

import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandProgress
import me.ivmg.telegram.entities.Update

class UpdateWithTextHandler<T : TelegramCommandProgress>(private val handler: (Update, String, T) -> Any) : UpdateHandler<T>() {

    override fun isApplicable(update: Update, progress: T): Boolean = update.message?.text != null

    override fun handle(update: Update, progress: T): Any {
        return handler(update, update.message!!.text!!, progress)
    }
}