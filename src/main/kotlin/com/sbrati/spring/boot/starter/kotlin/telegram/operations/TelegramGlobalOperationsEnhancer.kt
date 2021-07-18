package com.sbrati.spring.boot.starter.kotlin.telegram.operations

import com.github.kotlintelegrambot.entities.Update
import com.sbrati.spring.boot.starter.kotlin.telegram.model.callback.CallbackDataObject
import com.sbrati.telegram.domain.Event
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

class TelegramGlobalOperationsEnhancer {

    @Autowired
    lateinit var globalOperations: TelegramGlobalOperations

    val handlers: MutableList<GlobalCallbackHandler<*>> = ArrayList()
    val eventHandlers: MutableList<GlobalEventHandler<*>> = ArrayList()

    inline fun <reified E> event(noinline handler: (Event<E>) -> Any) {
        this.eventHandlers.add(GlobalEventHandler(E::class, handler))
    }

    inline fun <reified C : CallbackDataObject> callback(callbackData: String, noinline handler: (Update, C) -> Any) {
        handlers.add(GlobalCallbackHandler(C::class, callbackData, handler))
    }

    @PostConstruct
    private fun addToGlobalOperations() {
        globalOperations.handlers.addAll(handlers)
        globalOperations.eventHandlers.addAll(eventHandlers)
    }
}

fun addToGlobalOperations(builder: TelegramGlobalOperationsEnhancer.() -> Unit): TelegramGlobalOperationsEnhancer {
    return TelegramGlobalOperationsEnhancer().apply(builder)
}