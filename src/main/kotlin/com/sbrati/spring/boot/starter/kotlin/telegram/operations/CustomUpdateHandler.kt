package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import com.github.kotlintelegrambot.entities.Update

open abstract class CustomUpdateHandler {

    abstract fun isApplicable(update: Update): Boolean

    abstract fun onUpdate(update: Update): Any
}
