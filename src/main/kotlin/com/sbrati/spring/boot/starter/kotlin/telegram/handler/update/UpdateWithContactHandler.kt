package com.sbrati.spring.boot.starter.kotlin.telegram.handler.update

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.github.kotlintelegrambot.entities.Contact
import com.github.kotlintelegrambot.entities.Update

class UpdateWithContactHandler<T : Context>(private val handler: (Update, Contact, T) -> Any) : UpdateHandler<T>() {

    override fun isApplicable(update: Update, progress: T): Boolean = update.message?.contact != null

    override fun handle(update: Update, progress: T): Any {
        val contact = update.message!!.contact!!
        return handler(update, contact, progress)
    }
}