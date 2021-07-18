package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import com.github.kotlintelegrambot.entities.Update

open abstract class GlobalUpdateHandler {

    abstract fun onUpdate(update: Update)
}