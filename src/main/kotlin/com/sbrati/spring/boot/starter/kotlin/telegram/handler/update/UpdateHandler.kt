package com.sbrati.spring.boot.starter.kotlin.telegram.handler.update

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.github.kotlintelegrambot.entities.Update

abstract class UpdateHandler<T : Context> {

    abstract fun isApplicable(update: Update, progress: T): Boolean

    abstract fun handle(update: Update, progress: T): Any
}