package com.sbrati.spring.boot.starter.kotlin.telegram.handler

import com.sbrati.spring.boot.starter.kotlin.telegram.command.TelegramCommandProgress
import me.ivmg.telegram.entities.Update

abstract class UpdateHandler<T : TelegramCommandProgress> {

    abstract fun isApplicable(update: Update, progress: T): Boolean

    abstract fun handle(update: Update, progress: T): Any
}