package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import com.github.kotlintelegrambot.entities.Update
import com.sbrati.spring.boot.starter.kotlin.telegram.model.BanOptions
import com.sbrati.spring.boot.starter.kotlin.telegram.model.callback.CallbackDataObject
import com.sbrati.telegram.domain.Event

class TelegramGlobalOperations {

    val handlers: MutableList<GlobalCallbackHandler<*>> = ArrayList()
    val eventHandlers: MutableList<GlobalEventHandler<*>> = ArrayList()
    var banHandler: BanHandler? = null

    inline fun <reified E> event(noinline handler: (Event<E>) -> Any) {
        this.eventHandlers.add(GlobalEventHandler(E::class, handler))
    }

    inline fun <reified C : CallbackDataObject> callback(callbackData: String, noinline handler: (Update, C) -> Any) {
        handlers.add(GlobalCallbackHandler(C::class, callbackData, handler))
    }

    fun ban(handler: (Update, BanOptions) -> Any) {
        banHandler = BanHandler(handler)
    }
}

fun globalOperations(builder: TelegramGlobalOperations.() -> Unit): TelegramGlobalOperations {
    return TelegramGlobalOperations().apply(builder)
}