package com.sbrati.spring.boot.starter.kotlin.telegram.handler

import com.sbrati.spring.boot.starter.kotlin.telegram.command.Context
import com.sbrati.telegram.domain.Event
import kotlin.reflect.KClass

class EventHandler<P, T : Context>(private val type: KClass<*>, private val handler: (Event<P>, T) -> Any) {

    fun isApplicable(any: Any?): Boolean {
        return type.isInstance(any)
    }

    fun handle(event: Event<P>, progress: T): Any {
        return handler(event, progress)
    }
}