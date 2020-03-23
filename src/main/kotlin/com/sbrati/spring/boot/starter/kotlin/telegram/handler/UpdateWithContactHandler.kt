package com.sbrati.spring.boot.starter.kotlin.telegram.handler

import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandProgress
import me.ivmg.telegram.entities.Contact
import me.ivmg.telegram.entities.Update

class UpdateWithContactHandler<T : TelegramCommandProgress>(private val handler: (Update, Contact, T) -> Any) : UpdateHandler<T>() {

    override fun isApplicable(update: Update, progress: T): Boolean = update.message?.contact != null

    override fun handle(update: Update, progress: T): Any {
        val contact = update.message!!.contact!!
        return handler(update, contact, progress)
    }
}