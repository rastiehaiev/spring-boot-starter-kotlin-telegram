package com.sbrati.spring.boot.starter.kotlin.telegram.handler.update

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.entities.User

class UpdateWithForwardedUserHandler<T : Context>(private val handler: (Update, User, T) -> Any) : UpdateHandler<T>() {

    override fun isApplicable(update: Update, progress: T): Boolean = update.message?.forwardFrom != null

    override fun handle(update: Update, progress: T): Any {
        val forwardedUser = update.message?.forwardFrom!!
        return handler(update, forwardedUser, progress)
    }
}