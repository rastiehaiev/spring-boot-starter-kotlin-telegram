package com.sbrati.spring.boot.starter.kotlin.telegram.command

import com.sbrati.spring.boot.starter.kotlin.telegram.handler.*
import com.sbrati.spring.boot.starter.kotlin.telegram.model.callback.CallbackDataObject
import com.sbrati.telegram.domain.Event
import me.ivmg.telegram.entities.Contact
import me.ivmg.telegram.entities.Update

class TelegramCommandStage<T : Context>(val name: String) {

    var start: ((Update?, T) -> Any)? = null
    val handlers: MutableList<UpdateHandler<T>> = ArrayList()
    val eventHandlers: MutableList<EventHandler<*, T>> = ArrayList()

    fun start(start: (Update?, T) -> Any) {
        this.start = start
    }

    fun contact(handler: (Update, Contact, T) -> Any) {
        handlers.add(UpdateWithContactHandler(handler))
    }

    fun text(handler: (Update, String, T) -> Any) {
        handlers.add(UpdateWithTextHandler(handler))
    }

    fun update(handler: (Update, T) -> Any) {
        handlers.add(WholeUpdateHandler(handler))
    }

    inline fun <reified C : CallbackDataObject> callback(callbackData: String, noinline handler: (Update, C, T) -> Any) {
        handlers.add(UpdateWithCallbackHandler(C::class, callbackData, handler))
    }

    inline fun <reified E> event(noinline handler: (Event<E>, T) -> Any) {
        eventHandlers.add(EventHandler(E::class, handler))
    }
}